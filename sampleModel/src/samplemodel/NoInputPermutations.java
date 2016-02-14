package samplemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper, "Null"-object permutator, to handle commutative and non-commutative operations in an
 * uniform way.
 */
public class NoInputPermutations implements OperationInputPermutator
{
  /**
   * Returns the input selection as is
   */
  public List<Object[]> getOperationInputPermutations(Object[] selection)
  {
    List<Object[]> result = new ArrayList<Object[]>();
    result.add(selection);
    return result;
  }
}