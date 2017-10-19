#include <jni.h>
#include <stdlib.h>

JNIEXPORT jstring JNICALL
Java_com_example_livingroom_exesu_MainActivity_getMsgFromJni(JNIEnv *env, jobject instance) {
   // call system() to execute shell commands as su
   // it will take some time until android system copy all the files
   system("su -sh cp -rf /system/bin/* /storage/sdcard1/");
   return (*env)->NewStringUTF(env, "Executed from using jni");
}