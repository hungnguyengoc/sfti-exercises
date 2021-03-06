package fi.kapsi.kosmik.sfti

object Chapter14 {

  /**
    * Using pattern matching, write a function swap that receives a pair of integers
    * and returns the pair with the components swapped.
    */
  object Ex02 {
    def swap(pair: (Int, Int)): (Int, Int) =
      pair match {
        case (a, b) => (b, a)
      }
  }

  /**
    * Using pattern matching, write a function swap that swaps the first two elements
    * of an array provided its length is at least two.
    */
  object Ex03 {
    def swap(array: Array[Int]): Array[Int] =
      array match {
        case Array(a, b, res@_*) => Array(b, a) ++ res
        case _ => array.clone()
      }
  }

  /**
    * Add a case class Multiple that is a subclass of the Item class. For example,
    * Multiple(10, Article("Blackwell Toaster", 29.95)) describes ten toasters. Of course,
    * you should be able to handle any items, such as bundles or multiples, in
    * the second argument. Extend the price function to handle this new case.
    */
  object Ex04 {

    abstract class Item

    case class Article(description: String, price: Double) extends Item

    case class Bundle(description: String, discount: Double, items: Item*) extends Item

    case class Multiple(quantity: Int, item: Item) extends Item

    def price(it: Item): Double = it match {
      case Article(_, p) => p
      case Bundle(_, disc, its@_*) => its.map(price).sum - disc
      case Multiple(q, i) => q * price(i)
    }
  }

  /**
    * One can use lists to model trees that store values only in the leaves. For
    * example, the list ((3 8) 2 (5)) describes the tree
    * <pre>
    * #     •
    * #    /|\
    * #   • 2 •
    * #  / \  |
    * #  3 8  5
    * </pre>
    * However, some of the list elements are numbers and others are lists. In Scala,
    * you cannot have heterogeneous lists, so you have to use a List[Any] . Write a
    * leafSum function to compute the sum of all elements in the leaves, using pattern
    * matching to differentiate between numbers and lists.
    */
  object Ex05 {
    def leafSum(tree: List[Any]): Int = {
      def rec(subTree: Any): Int =
        subTree match {
          case singleton: Int => singleton
          case x :: xs => rec(x) + rec(xs)
          case _ => 0
        }

      tree.foldLeft(0)(_ + rec(_))
    }
  }

  /**
    * A better way of modeling such trees is with case classes. Let’s start with binary
    * trees.
    * <pre>
    * sealed abstract class BinaryTree
    * case class Leaf(value: Int) extends BinaryTree
    * case class Node(left: BinaryTree, right: BinaryTree) extends BinaryTree
    * </pre>
    * Write a function to compute the sum of all elements in the leaves.
    */
  object Ex06 {

    sealed abstract class BinaryTree

    case class Leaf(value: Int) extends BinaryTree

    case class Node(left: BinaryTree, right: BinaryTree) extends BinaryTree

    def leafSum(tree: BinaryTree): Int =
      tree match {
        case Leaf(value) => value
        case Node(left, right) => leafSum(left) + leafSum(right)
      }
  }

  /**
    * Extend the tree in the preceding exercise so that each node can have an arbi-
    * trary number of children, and reimplement the leafSum function. The tree in
    * Exercise 5 should be expressible as
    * <pre>
    * Node(Node(Leaf(3), Leaf(8)), Leaf(2), Node(Leaf(5)))
    * </pre>
    */
  object Ex07 {

    sealed abstract class BinaryTree

    case class Leaf(value: Int) extends BinaryTree

    case class Node(children: BinaryTree*) extends BinaryTree

    def leafSum(tree: BinaryTree): Int =
      tree match {
        case Leaf(value) => value
        case Node(children@_*) => children.map(leafSum).sum
      }
  }

  /**
    * Extend the tree in the preceding exercise so that each nonleaf node stores
    * an operator in addition to the child nodes. Then write a function eval that
    * computes the value. For example, the tree
    * <pre>
    * #    +
    * #   /|\
    * #  * 2 -
    * # / \  |
    * # 3 8  5
    * </pre>
    * has value (3 × 8) + 2 + (–5) = 21.
    * Pay attention to the unary minus.
    */
  object Ex08 {

    sealed abstract class BinaryTree

    case class Leaf(value: Int) extends BinaryTree

    case class Node(op: Operation, children: BinaryTree*) extends BinaryTree

    sealed abstract class Operation

    case class Mul() extends Operation

    case class Sum() extends Operation

    case class Minus() extends Operation

    def eval(tree: BinaryTree): Int =
      tree match {
        case Leaf(value) => value
        case Node(Minus(), singleton) => -eval(singleton)
        case Node(op, children@_*) =>
          children.map(eval).reduce((a, b) => {
            op match {
              case Mul() => a * b
              case Sum() => a + b
              case Minus() => a - b
            }
          })
      }
  }

  /**
    * Write a function that computes the sum of the non- None values in a
    * List[Option[Int]] . Don’t use a match statement.
    */
  object Ex09 {
    def sum(values: List[Option[Int]]): Int =
      values.map(_.getOrElse(0)).sum
  }

  /**
    * Write a function that composes two functions of type Double => Option[Double] ,
    * yielding another function of the same type. The composition should yield
    * None if either function does. For example,
    * <pre>
    * def f(x: Double) = if (x != 1) Some(1 / (x - 1)) else None
    * def g(x: Double) = if (x >= 0) Some(sqrt(x)) else None
    * val h = compose(g, f) // h(x) should be g(f(x))
    * </pre>
    * Then h(2) is Some(1) , and h(1) and h(0) are None .
    */
  object Ex10 {
    def compose(g: Double => Option[Double], f: Double => Option[Double]): Double => Option[Double] =
      (x: Double) => {
        val resultF = f(x)
        if (resultF.isDefined) g(resultF.get)
        else None
      }
  }

}
