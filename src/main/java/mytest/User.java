/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mytest;

import java.io.Serializable;

/**
 * 内测版登录，用户账户信息
 * 
 * @author wangyong
 * @version [1.0.0, 2018年4月3日]
 * @since [咪咕游戏/模块版本]
 */
public class User implements Serializable {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    private Long              id;

    /**
     * 账号名
     */
    private Long            miId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMiId() {
        return miId;
    }

    public void setMiId(Long miId) {
        this.miId = miId;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", miId=" + miId + '}';
    }
}
