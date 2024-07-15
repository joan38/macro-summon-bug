import scala.quoted.*

inline def summonValuesInPackage(inline packageName: String): Seq[?] = ${ summonValuesInPackageImpl('packageName) }

private def summonValuesInPackageImpl(packageName: Expr[String])(using Quotes): Expr[Seq[?]] =
  import quotes.reflect.*

  def findAllTypesInPackage(pkg: Symbol): Seq[TypeRepr] =
    pkg.declarations.flatMap:
      case sym if sym.isPackageDef || sym.flags.is(Flags.Module) => findAllTypesInPackage(sym) // Recurse
      case sym
          if sym.isType || sym.isClassDef || sym.flags.is(Flags.Opaque) || sym.isAliasType || sym.isTypeDef || sym.isAbstractType =>
        Seq(sym.typeRef) // Include
      case _ => Seq.empty // Skip

  // Get the package where we will look for types
  val typesPackage = Symbol.requiredPackage(packageName.valueOrAbort)

  // Find all types in the package and summon them
  val values = findAllTypesInPackage(typesPackage).flatMap: typeRepr =>
    typeRepr.asType match
      case '[t] => Expr.summon[t] // This does not seem to work when `t` is an opaque type

  Expr.ofSeq(values)
