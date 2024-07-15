package use

opaque type MyOpaqueType = Boolean

object MyOpaqueType:
  given MyOpaqueType = true
