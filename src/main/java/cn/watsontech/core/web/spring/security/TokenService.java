package cn.watsontech.core.web.spring.security;

import org.springframework.security.core.token.Token;

/**
 * Provides a mechanism to allocate and rebuild secure, randomised tokens.
 *
 * <p>
 * Implementations are solely concern with issuing a new {@link Token} on demand. The
 * issued <code>Token</code> may contain user-specified extended information. The token
 * also contains a cryptographically strong, byte array-based key. This permits the token
 * to be used to identify a user session, if desired. The key can subsequently be
 * re-presented to the <code>TokenService</code> for verification and reconstruction of a
 * <code>Token</code> equal to the original <code>Token</code>.
 * </p>
 *
 * <p>
 * Given the tightly-focused behaviour provided by this interface, it can serve as a
 * building block for more sophisticated token-based solutions. For example,
 * authentication systems that depend on stateless session keys. These could, for
 * instance, place the username inside the user-specified extended information associated
 * with the key). It is important to recognise that we do not intend for this interface to
 * be expanded to provide such capabilities directly.
 * </p>
 *
 *
 */
public interface TokenService {
    /**
     * Forces the allocation of a new {@link Token}.
     *
     * @param loginUser the extended information desired in the token (cannot be
     * <code>null</code>, but can be empty)
     * @param timeToLive time to live in seconds
     * @return a new token that has not been issued previously, and is guaranteed to be
     * recognised by this implementation's {@link #verifyToken(String)} at any future
     * time.
     */
    Token allocateToken(LoginUser loginUser, Long timeToLive);

    /**
     * Permits verification the {@link Token#getKey()} was issued by this
     * <code>TokenService</code> and reconstructs the corresponding <code>Token</code>.
     *
     * @param key as obtained from {@link Token#getKey()} and created by this
     * implementation
     * @return the token, or <code>null</code> if the token was not issued by this
     * <code>TokenService</code>
     */
    Token verifyToken(String key);
}
