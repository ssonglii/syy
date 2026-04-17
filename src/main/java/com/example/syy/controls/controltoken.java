package com.example.syy.controls;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.syy.annotation.PassToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController //？？？
public class controltoken {
   // @Autowired
    //测试无passtoken
    @RequestMapping(value="/bbs/test",method = RequestMethod.GET)
    public String demotest(HttpServletRequest request){
        //获取拦截器拦截下来的token的用户信息
        System.out.print("\ntest收到的token:"+request.getAttribute("id")+"\n");
        return request.getHeader("token");
//        return request.getAttribute("id");
    }
    //登录模块,登录不需要token验证，加入passtoken注解
    @PassToken//本系统默认是对/bbs所请求都拦截，但加上此注解下面的请求则不通过token判断 session的
    @GetMapping ("/bbs/reToken")
    public String Login(){
        Date start = new Date();
        long currrentTime =System.currentTimeMillis()+60*60*1000;
        Date end = new Date(currrentTime);
        Date d1=new Date();String T=d1.toString();
        String token= JWT.create ().withAudience ("zhangsan").withIssuedAt (start)
                .withExpiresAt (end)
                .sign (Algorithm.HMAC256 ("zhangsan"));//私钥
        return token;  //再在前端js中写一个方法，每个页面或请求调用带上token传给后端
    }
}
