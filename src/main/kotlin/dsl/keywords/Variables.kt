@file:Suppress("ClassName", "PackageDirectoryMismatch")

package agct

object initial {
    val concentration = Concentration()
    class Concentration internal constructor()
}

object degradation {
    val rate = Rate()
    class Rate internal constructor()
}

object basal {
    val rate = Rate()
    class Rate internal constructor()
}

object regulating {
    val rate = Rate()
    class Rate internal constructor()
}

object binding {
    val rate = Rate()
    class Rate internal constructor()
}

object unbinding {
    val rate = Rate()
    class Rate internal constructor()
}

object reaction {
    val rate = Rate()
    class Rate internal constructor()
}
