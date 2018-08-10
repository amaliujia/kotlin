package test

fun foo(x: Int, vararg y: String) {}

fun use0(f: (Int) -> Unit) {}
fun use1(f: (Int, String) -> Unit) {}
fun use2(f: (Int, String, String) -> Unit) {}

fun kotlinSamConversions(r: Runnable) {}

fun testNewInference() {
    // should be OK only in the new inference
    use0(::foo)
    use1(::foo)
    use2(::foo)

    kotlinSamConversions { }
}