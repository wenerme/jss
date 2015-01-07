/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package jss.proto.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 * The main idea is that no password are sent between client & server on
 * connection and that no password are saved in mysql in a decodable form.
 *
 * On connection a random string is generated and sent to the client.
 * The client generates a new string with a random generator inited with
 * the hash values from the password and the sent string.
 * This 'check' string is sent to the server where it is compared with
 * a string generated from the stored hash_value of the password and the
 * random string.
 *
 * The password is saved (in user.password) by using the PASSWORD() function in
 * mysql.
 *
 * This is .c file because it's used in libmysqlclient, which is entirely in C.
 * (we need it to be portable to a variety of systems).
 * Example:
 * update user set password=PASSWORD("hello") where user="test"
 * This saves a hashed number as a string in the password field.
 *
 * The new authentication is performed in following manner:
 *
 * SERVER:  public_seed=create_random_string()
 * send(public_seed)
 *
 * CLIENT:  recv(public_seed)
 *          hash_stage1=sha1("password")
 *          hash_stage2=sha1(hash_stage1)
 *          reply=xor(hash_stage1, sha1(public_seed,hash_stage2)
 *
 *          // this three steps are done in scramble()
 *
 *          send(reply)
 *
 *
 *
 * SERVER:  recv(reply)
 *          hash_stage1=xor(reply, sha1(public_seed,hash_stage2))
 *          candidate_hash2=sha1(hash_stage1)
 *          check(candidate_hash2==hash_stage2)
 *
 *          // this three steps are done in check_scramble()
 * </pre>
 */
@SuppressWarnings("unused")
public class SecurityUtil
{
    public static byte[] scramble411(byte[] pass, byte[] seed) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] pass1 = md.digest(pass);
        md.reset();
        byte[] pass2 = md.digest(pass1);
        md.reset();
        md.update(seed);
        byte[] pass3 = md.digest(pass2);
        for (int i = 0; i < pass3.length; i++)
        {
            pass3[i] = (byte) (pass3[i] ^ pass1[i]);
        }
        return pass3;
    }

    /**
     * Scramble string with password.
     * Used in pre 4.1 authentication phase.
     *
     * @param message  IN  Message to scramble. Message must be at least
     *                 SRAMBLE_LENGTH_323 bytes long.
     * @param password IN  Password to use while scrambling
     * @return OUT Store scrambled message here. Buffer must be at least
     * SCRAMBLE_LENGTH_323+1 bytes long
     */
    public static String scramble323(String password, String message)
    {
        if ((password == null) || (password.length() == 0))
        {
            return password;
        }
        byte b;
        double d;
        long[] pw = hashPassword(message);
        long[] msg = hashPassword(password);
        long max = 0x3fffffffL;
        long seed1 = (pw[0] ^ msg[0]) % max;
        long seed2 = (pw[1] ^ msg[1]) % max;
        char[] chars = new char[message.length()];
        for (int i = 0; i < message.length(); i++)
        {
            seed1 = ((seed1 * 3) + seed2) % max;
            seed2 = (seed1 + seed2 + 33) % max;
            d = (double) seed1 / (double) max;
            b = (byte) Math.floor((d * 31) + 64);
            chars[i] = (char) b;
        }
        seed1 = ((seed1 * 3) + seed2) % max;
        seed2 = (seed1 + seed2 + 33) % max;
        d = (double) seed1 / (double) max;
        b = (byte) Math.floor(d * 31);
        for (int i = 0; i < message.length(); i++)
        {
            chars[i] ^= (char) b;
        }
        return new String(chars);
    }

    /**
     * Generate binary hash from raw text string
     * Used for Pre-4.1 password handling
     * <p/>
     *
     * @param password IN  plain text password to build hash
     * @return OUT store hash in this location
     */

    //    password_len IN  password length (password may be not null-terminated)
    //    private static long[] hashPassword(String password, int passwordLen)
    public static long[] hashPassword(String password)
    {
        long nr = 1345345333L;
        long add = 7;
        long nr2 = 0x12345671L;
        long tmp;
        for (int i = 0; i < password.length(); ++i)
        {
            switch (password.charAt(i))
            {
                case ' ':
                case '\t':
                    continue;
                default:
                    tmp = (0xff & password.charAt(i));
                    nr ^= ((((nr & 63) + add) * tmp) + (nr << 8));
                    nr2 += ((nr2 << 8) ^ nr);
                    add += tmp;
            }
        }
        long[] result = new long[2];
        result[0] = nr & 0x7fffffffL;
        result[1] = nr2 & 0x7fffffffL;
        return result;
    }

}