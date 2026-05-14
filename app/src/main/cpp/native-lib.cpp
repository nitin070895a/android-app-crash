#include <jni.h>
#include <string>

/**
 * Out of bound crash simulation by accessing the non existing array element
 */
void outOfBound() {
    int arr[] = {1, 2};
    arr[1] = 4;
    arr[10000] = 10; // This will crash with out of bound exception
}

/**
 * Out of memory crash simulation by writing to a memory location outside the application scope
 */
void outOfMemory() {
    uint64_t* faultyAddress = (uint64_t*) 0x414141414141;
    *faultyAddress = 1111; // This will crash as writing at a faulty address
}

/**
 * Null pointer dereference crash simulation by dereferencing a null pointer and writing to it
 */
void nullPointerDereference() {
    int* ptr = nullptr;
    *ptr = 1; // This will crash as dereferencing a null pointer
}

/**
 * The Bridge function that gets triggered from kotlin upon clicking any of the crash function
 * @param type The type of crash to perform
 */
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

/**
 * Sample bridge between kotlin/java and c++
 */
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_androidappcrash_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}