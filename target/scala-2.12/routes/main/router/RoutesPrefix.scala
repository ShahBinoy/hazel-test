// @GENERATOR:play-routes-compiler
// @SOURCE:/home/shahb/workstation/workspace/arc-subscriptions-workspace/entdev/hazel-test/conf/routes
// @DATE:Fri Sep 21 11:37:27 EDT 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
