package cn.watsontech.webhelper.common.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

/**
 * Basic implementation of {@link TokenService} that is compatible with clusters and
 * across machine restarts, without requiring database persistence.
 *
 * <p>
 * Keys are produced in the format:
 * </p>
 *
 * <p>
 * Base64(creationTime + ":" + expireTime + ":" + hex(pseudoRandomNumber) + ":" + extendedInformation + ":" +
 * Sha512Hex(creationTime + ":" + expireTime + ":" + hex(pseudoRandomNumber) + ":" + extendedInformation +
 * ":" + serverSecret) )
 * </p>
 *
 * <p>
 * In the above, <code>creationTime</code>, <code>tokenKey</code> and
 * <code>extendedInformation</code> are equal to that stored in {@link Token}. The
 * <code>Sha512Hex</code> includes the same payload, plus a <code>serverSecret</code>.
 * </p>
 *
 * <p>
 * The <code>serverSecret</code> varies every millisecond. It relies on two static
 * server-side secrets. The first is a password, and the second is a server integer. Both
 * of these must remain the same for any issued keys to subsequently be recognised. The
 * applicable <code>serverSecret</code> in any millisecond is computed by
 * <code>password</code> + ":" + (<code>creationTime</code> % <code>serverInteger</code>).
 * This approach further obfuscates the actual server secret and renders attempts to
 * compute the server secret more limited in usefulness (as any false tokens would be
 * forced to have a <code>creationTime</code> equal to the computed hash). Recall that
 * framework features depending on token services should reject tokens that are relatively
 * old in any event.
 * </p>
 *
 * <p>
 * A further consideration of this class is the requirement for cryptographically strong
 * pseudo-random numbers. To this end, the use of {@link SecureRandomFactoryBean} is
 * recommended to inject the property.
 * </p>
 *
 * <p>
 * This implementation uses UTF-8 encoding internally for string manipulation.
 * </p>
 *
 * @author Ben Alex
 *
 */
public class TimeKeyBasedPersistenceTokenService implements TokenService, InitializingBean {
	Log log = LogFactory.getLog(TimeKeyBasedPersistenceTokenService.class);

	private int pseudoRandomNumberBytes = 32;
	private String serverSecret;
	private Integer serverInteger;
	private SecureRandom secureRandom;

	/**
	 * 仅使用loginUser的id和userType
	 * @param loginUser the extended information desired in the token (cannot be
	 * <code>null</code>, but can be empty)
	 * @param timeToLive time to live in seconds
	 * @return
	 */
	@Override
	public Token allocateToken(LoginUser loginUser, Long timeToLive/*token有效时间*/) {
		Assert.notNull(loginUser, "Must provided non-null extendedInformation (but it can be empty)");
		if (timeToLive==null) {
			timeToLive = 1800000l;//默认 30分钟
		}

		long creationTime = new Date().getTime();
		long expireTime = creationTime + timeToLive;//过期时间
		String extendedInformation = loginUser.getId()+"@"+loginUser.getUserType();//token主要内容：userId@userType, e. 2983742@user

		String serverSecret = computeServerSecretApplicableAt(creationTime);
		String pseudoRandomNumber = generatePseudoRandomNumber();
		String content = creationTime + ":" + expireTime + ":" + pseudoRandomNumber + ":"
				+ extendedInformation;

		// Compute key
		String sha512Hex = Sha512DigestUtils.shaHex(content + ":" + serverSecret);
		String keyPayload = content + ":" + sha512Hex;
		String key = Utf8.decode(Base64.getEncoder().encode(Utf8.encode(keyPayload)));

		return new DefaultToken(key, creationTime, String.valueOf(loginUser.getId()));
	}

	public Token verifyToken(String key) {
		if (key == null || "".equals(key)) {
			return null;
		}

		String[] tokens;
		try {
			tokens = StringUtils.delimitedListToStringArray(Utf8.decode(Base64.getDecoder().decode(Utf8.encode(key))), ":");
		}catch (IllegalArgumentException ex) {
			log.error(String.format("Invalid Token, {}", ex.getMessage()));

			throw new BadCredentialsException("无效的访问Token, " + ex.getMessage());
		}

		if (tokens.length < 5) {
			throw new BadCredentialsException("无效的访问Token, Expected 5 or more tokens but found " + tokens.length);
		}

		long creationTime, expireTime;
		try {
			creationTime = Long.decode(tokens[0]);
			expireTime = Long.decode(tokens[1]);
		} catch (NumberFormatException nfe) {
			log.error(String.format("Invalid token, number format exception {}", nfe.getMessage()));

			throw new BadCredentialsException("无效的访问Token, Expected number but found " + tokens[0]+" or "+tokens[1]);
		}

		// Permit extendedInfo to itself contain ":" characters
		StringBuilder extendedInfo = new StringBuilder();
		for (int i = 3; i < tokens.length - 1; i++) {
			if (i > 3) {
				extendedInfo.append(":");
			}
			extendedInfo.append(tokens[i]);
		}

		if (expireTime<new Date().getTime()) {
			log.info(String.format("Token expired, accountInfo = {}", extendedInfo.toString()));
			throw new BadCredentialsException("登录已过期，请重新登录");
		}

		String serverSecret = computeServerSecretApplicableAt(creationTime);
		String pseudoRandomNumber = tokens[2];

		String sha1Hex = tokens[tokens.length - 1];

		// Verification
		String content = creationTime + ":" + expireTime + ":" + pseudoRandomNumber + ":" + extendedInfo.toString();
		String expectedSha512Hex = Sha512DigestUtils.shaHex(content + ":" + serverSecret);

		if (!expectedSha512Hex.equals(sha1Hex)) {
			log.info(String.format("Key verification failure, accountInfo = {}", extendedInfo.toString()));
			throw new BadCredentialsException("Token验证失败，拒绝访问");
		}

		return new DefaultToken(key, creationTime, extendedInfo.toString());
	}

	/**
	 * @return a pseduo random number (hex encoded)
	 */
	private String generatePseudoRandomNumber() {
		byte[] randomBytes = new byte[pseudoRandomNumberBytes];
		secureRandom.nextBytes(randomBytes);
		return new String(Hex.encode(randomBytes));
	}

	private String computeServerSecretApplicableAt(long time) {
		return serverSecret + ":" + new Long(time % serverInteger).intValue();
	}

	/**
	 * @param serverSecret the new secret, which can contain a ":" if desired (never being
	 * sent to the client)
	 */
	public void setServerSecret(String serverSecret) {
		this.serverSecret = serverSecret;
	}

	public void setSecureRandom(SecureRandom secureRandom) {
		this.secureRandom = secureRandom;
	}

	/**
	 * @param pseudoRandomNumberBytes changes the number of bytes issued (must be &gt;= 0;
	 * defaults to 256)
	 */
	public void setPseudoRandomNumberBytes(int pseudoRandomNumberBytes) {
		Assert.isTrue(pseudoRandomNumberBytes >= 0,
				"Must have a positive pseudo random number bit size");
		this.pseudoRandomNumberBytes = pseudoRandomNumberBytes;
	}

	public void setServerInteger(Integer serverInteger) {
		this.serverInteger = serverInteger;
	}

	public void afterPropertiesSet() {
		Assert.hasText(serverSecret, "Server secret required");
		Assert.notNull(serverInteger, "Server integer required");
		Assert.notNull(secureRandom, "SecureRandom instance required");
	}
}
