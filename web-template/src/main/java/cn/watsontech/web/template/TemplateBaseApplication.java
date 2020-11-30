package cn.watsontech.web.template;

import cn.watsontech.web.template.service.AdminService;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.security.UserTypeFactory;
import cn.watsontech.webhelper.common.security.authentication.AccountService;
import cn.watsontech.webhelper.utils.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(value = {"cn.watsontech.web.template.mapper"},
		properties = {
				"mappers=tk.mybatis.mapper.common.Mapper,tk.mybatis.mapper.common.ConditionMapper,tk.mybatis.mapper.common.IdsMapper,tk.mybatis.mapper.common.MySqlMapper,cn.watsontech.webhelper.mybatis.mapper.BatchInsertMapper,cn.watsontech.webhelper.mybatis.mapper.UseGenerateKeyInsertMapper",
				"notEmpty=false","useSimpleType=false"
		}
)
public class TemplateBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateBaseApplication.class, args);
	}

	@RestController
	class EchoController {
		@GetMapping("/echo/{string}")
		public String echo(@PathVariable String string) {
			return "hello Api Template（模版项目） Discovery " + string;
		}

	}

	//启用账号登陆，默认用户名密码：admin@admin/A123456，系统以@userType 为识别标示，标示各账户体系
	@Bean
	public AccountService accountService() {
		return new AccountService();
	}

	//登陆密码加密算法
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//配置账号登陆用户类型，内置admin/user类型，对应loginUserService为adminService和userService
	@Bean
	public UserTypeFactory userTypeFactory(@Autowired AdminService adminService) {
		return new UserTypeFactory(MapBuilder.builder().putNext(LoginUser.Type.admin, adminService));
	}
}
