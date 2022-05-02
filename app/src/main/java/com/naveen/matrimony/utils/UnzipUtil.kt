package com.naveen.matrimony.utils

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class UnzipUtil(private val zipFile: String?, private val location: String?) {
    fun init() {
        dirChecker("")
    }

    fun unzip() {
        try {
            val fin = FileInputStream(zipFile!!)
            val zin = ZipInputStream(fin)
            var ze: ZipEntry? = null
            while (zin.nextEntry.also { ze = it } != null) {
                Log.v("Decompress", "Unzipping " + ze?.name)
                if (ze?.isDirectory!!) {
                    ze?.name?.let { dirChecker(it) }
                } else {
                    val fout = FileOutputStream(location + ze?.name)
                    val buffer = ByteArray(8192)
                    var len: Int = 0
                    while (zin.read(buffer).also { len = it } != -1) {
                        fout.write(buffer, 0, len)
                    }
                    fout.close()
                    zin.closeEntry()
                }
            }
            zin.close()
        } catch (e: Exception) {
            Log.e("Decompress", "unzip", e)
        }
    }

    private fun dirChecker(dir: String) {
        val f = File(location + dir)
        if (!f.isDirectory) {
            f.mkdirs()
        }
    }
}