package com.za.caretogether.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object ChCrypto{
    @JvmStatic fun tryMeHack(v:String, secretKey:String) = AES256.tryMeHack(v, secretKey)
    @JvmStatic fun tryMeHackMore(v:String, secretKey:String) = AES256.tryMeHackMore(v, secretKey)
}

private object AES256{

    private fun cipher(opmode:Int, secretKey:String):Cipher{
        if(secretKey.length != 32) throw RuntimeException("SecretKey length is not 32 chars")
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        val iv = IvParameterSpec(secretKey.substring(0, 16).toByteArray(Charsets.UTF_8))
        c.init(opmode, sk, iv)
        return c
    }
    fun tryMeHack(str:String, secretKey:String):String{
        val encrypted = cipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(str.toByteArray(Charsets.UTF_8))
        return android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT)
      //  return String(encorder.encode(encrypted))
        return android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT)
    }

    fun tryMeHackMore(str:String, secretKey:String):String{

        val byteStr = android.util.Base64.decode(str.toByteArray(Charsets.UTF_8),android.util.Base64.DEFAULT)
        return String(cipher(Cipher.DECRYPT_MODE, secretKey).doFinal(byteStr))
    }
}