package com.pinyougou.shop.service;

import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户认证服务类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-01<p>
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        System.out.println("sellerService = " + sellerService);

        // 根据username查询seller对象
        Seller seller = sellerService.findOne(username);

        // 判断商家是否为空，状态码
        if (seller != null && "1".equals(seller.getStatus())) {

            // 定义List集合封装角色权限
            List<GrantedAuthority> authorities = new ArrayList<>();
            // 添加角色
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

            return new User(username, seller.getPassword(), authorities);
        }
        return null;
    }

    /** spring的setter注入 */
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
}
