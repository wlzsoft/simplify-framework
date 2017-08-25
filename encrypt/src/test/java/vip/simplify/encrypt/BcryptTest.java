package vip.simplify.encrypt;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;


/**
 * <p><b>Title:</b><i>最安全可靠的哈希签名算法 测试</i></p>
 * <p>Desc: jBCrypt 是基于 blowfish变行而来的
 jBCrypt is a Java™ implementation of OpenBSD's Blowfish password hashing code, as described in "A Future-Adaptable Password Scheme" by Niels Provos and David Mazières.

 This system hashes passwords using a version of Bruce Schneier's Blowfish block cipher with modifications designed to raise the cost of off-line password cracking and frustrate fast hardware implementation. The computation cost of the algorithm is parametised, so it can be increased as computers get faster. The intent is to make a compromise of a password database less likely to result in an attacker gaining knowledge of the plaintext passwords (e.g. using John the Ripper).

 There seems to be a lack of good password hashes for Java - the top two hits in Google (as of 2006/05/24) for "Java password hash" and "Java password encryption" both offer terrible advice: one uses an unsalted hash which allows reverse dictionary lookup of passwords and the other recommends reversible encryption, which is rarely needed and should only be used as a last resort.

 jBCrypt is licensed under a ISC/BSD licence (see the LICENSE file for details) and ships with a set of JUnit unit tests to verify correct operation of the library and compatibility with the canonical C implementation of the bcrypt algorithm.</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:07:13</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:07:13</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BcryptTest {
    @Test
    public void test() {
        String password = "1234568lcy";
        // Hash a password for the first time
        //String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        // gensalt's log_rounds parameter determines the complexity
        // the work factor is 2**log_rounds, and the default is 10
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Check that an unencrypted password matches one that has
        // previously been hashed
        if (BCrypt.checkpw("1234568lcy", hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }
}
