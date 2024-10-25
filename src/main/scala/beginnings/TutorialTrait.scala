package beginnings

trait TutorialTrait {
  
  def index(): Int
  
}

object TutorialTrait {

  private class FirstTrait extends TutorialTrait {
    override def index(): Int = 1
  }

  private class SecondTrait extends TutorialTrait {
    override def index(): Int = 2
  }

  private class ThirdTrait extends TutorialTrait {
    override def index(): Int = 3
  }

  // factory method
  def apply(in:TutorialEnum): TutorialTrait = {
    in match {
      case TutorialEnum.Value1 => new FirstTrait
      case TutorialEnum.Value2 => new SecondTrait
      case TutorialEnum.Value3 => new ThirdTrait
      case null => null
    }
  }

}
