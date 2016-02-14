package samplemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EachElementVersusTheRest implements OperationInputPermutator
{

  @Override
  public List<Object[]> getOperationInputPermutations(Object[] selection)
  {

    List<Object[]> permutations = new ArrayList<Object[]>();
    for (Object o : selection)
    {
      List<Object> permutation = new ArrayList<>();
      permutation.add(o);

      List<Object> rest = new ArrayList<Object>(Arrays.asList(selection));
      rest.remove(o);
      permutation.addAll(rest);

      permutations.add(permutation.toArray());
    }

    return permutations;
  }

}
