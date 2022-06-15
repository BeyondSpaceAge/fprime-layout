package fpl.util

object Result {

  type Result[T] = Either[String, T]

  /** Apply a result function to each element of a list */
  def map[A, B](
      list: List[A],
      f: A => Result[B]
  ): Result[List[B]] = {
    def helper(in: List[A], out: List[B]): Result[List[B]] = {
      in match {
        case Nil => Right(out)
        case head :: tail =>
          f(head) match {
            case Left(e)  => Left(e)
            case Right(b) => helper(tail, b :: out)
          }
      }
    }
    helper(list.reverse, Nil)
  }

  /** Apply a list of result functions in sequence */
  def seq[A](
      r: Result[A],
      fs: List[A => Result[A]]
  ): Result[A] = {
    (r, fs) match {
      case (Right(a), f :: fs1) => seq(f(a), fs1)
      case _                    => r
    }
  }

}
