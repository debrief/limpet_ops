package samplemodel;

import java.util.List;

/**
 * Non-commutative operations could define an {@link OperationInputPermutator} to handle the input
 * in a different way. For example a subtract operation could act in two different ways on the same
 * selection - subtract first from the second or second from the first.
 * 
 * @see EachElementVersusTheRest
 *
 */
public interface OperationInputPermutator
{
      /**
       * @param selection
       *          array of selected object
       * @return the list of valid permutations on which the operation can be applied
       */
      List<Object[]> getOperationInputPermutations(Object[] selection);
}
