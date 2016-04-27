package samplemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * For a selection of <b>n</b> elements, will return <b>n</b> permutations each starting with
 * <b>selection[n]</b> element and followed by the rest of the elements. For example selection
 * [1,2,3] produces permutations [[1,2,3],[2,1,3],[3,1,2]]
 * 
 */
public class EachElementVersusTheRest implements OperationInputPermutator
{

  @Override
  public List<Object[]> getOperationInputPermutations(Object[] selection)
  {

    List<Object[]> permutations = new ArrayList<Object[]>();
    // still needs to include one empty selection
    if (selection.length == 0)
    {
      permutations.add(selection);
    }
    else
    {
      for (Object o : selection)
      {
        List<Object> permutation = new ArrayList<>();
        permutation.add(o);

        List<Object> rest = new ArrayList<Object>(Arrays.asList(selection));
        rest.remove(o);
        permutation.addAll(rest);

        permutations.add(permutation.toArray());
      }
    }

    return permutations;
  }

}
