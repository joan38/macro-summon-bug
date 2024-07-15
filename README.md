# macro-summon-bug

This repo is to demo a potential bug in Scala 3 Macro's Expr.summon that does not summon opaque types.

Run `./scala .` and no value for `MyOpaqueType` is collected but there are 3 (not sure why 3) for `MyCaseClass`.
