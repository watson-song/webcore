package cn.watsontech.core.web.spring.security;

import cn.watsontech.core.web.spring.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 用户类型工厂
 * Created by Watson on 2020/8/12.
 */
public class UserTypeFactory {

    Set<IUserType> allUserTypes = new HashSet<>();

    public UserTypeFactory(Set<IUserType> extraUserTypes) {
        allUserTypes.addAll(Arrays.asList(LoginUser.Type.values()));

        if (allUserTypes!=null) {
            allUserTypes.addAll(extraUserTypes);
        }
    }

    /**
     * 获取用户类型
     * @param userType
     */
    public IUserType valueOf(String userType) {
        if (StringUtils.isEmpty(userType)) return LoginUser.Type.user;

        Optional<IUserType> optionUserType = allUserTypes.stream().filter(type -> userType.equals(type.name())).findFirst();
        Assert.isTrue(optionUserType.isPresent(), "不能识别的用户类型："+userType);

        return optionUserType.get();
    }
}
