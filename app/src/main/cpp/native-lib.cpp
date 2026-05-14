#include <jni.h>
#include <string>

void outOfBound() {
    printf("Out of bound crash");
    int arr[] = {1, 2};
    arr[1] = 4;
    arr[10000] = 10; // This will crash with out of bound exception
}

void outOfMemory() {
    printf("outOfMemory crash");
    uint64_t* faultyAddress = (uint64_t*) 0x414141414141;
    *faultyAddress = 1111;
}

void nullPointerDereference() {
    printf("nullPointerDereference crash");
    int* ptr = nullptr;
    *ptr = 1;
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_androidappcrash_MainActivity_doCrash(JNIEnv* env, jobject, jint type) {

    switch (type) {
        case 1: {
            outOfBound();
            break;
        }
        case 2: {
            outOfMemory();
            break;
        }
        case 3: {
            nullPointerDereference();
            break;
        }
        default: {
            // All good
        }
    }
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_androidappcrash_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}