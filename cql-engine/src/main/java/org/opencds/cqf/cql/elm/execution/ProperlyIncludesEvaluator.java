package org.opencds.cqf.cql.elm.execution;

import java.util.stream.StreamSupport;

import org.opencds.cqf.cql.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
*** NOTES FOR INTERVAL ***
properly includes _precision_ (left Interval<T>, right Interval<T>) Boolean

The properly includes operator for intervals returns true if the first interval completely includes the second
    and the first interval is strictly larger than the second. More precisely, if the starting point of the first interval
    is less than or equal to the starting point of the second interval, and the ending point of the first interval is
    greater than or equal to the ending point of the second interval, and they are not the same interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
properly includes(left List<T>, right List<T>) Boolean

The properly includes operator for lists returns true if the first list contains every element of the second list, a
    nd the first list is strictly larger than the second list.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If the left argument is null, the result is false, else if the right argument is null, the result is true if the left argument is not empty.
Note that the order of elements does not matter for the purposes of determining inclusion.
*/

public class ProperlyIncludesEvaluator extends org.cqframework.cql.elm.execution.ProperIncludes {

    public static Boolean properlyIncludes(Object left, Object right, String precision) {
        if (left == null && right == null) {
            return null;
        }

        if (left == null) {
            return right instanceof Interval
                    ? intervalProperlyIncludes(null, (Interval) right, precision)
                    : listProperlyIncludes(null, (Iterable) right);
        }

        if (right == null) {
            return left instanceof Interval
                    ? intervalProperlyIncludes((Interval) left, null, precision)
                    : listProperlyIncludes((Iterable) left, null);
        }

        if (left instanceof Interval && right instanceof Interval) {
            return intervalProperlyIncludes((Interval) left, (Interval) right, precision);
        }
        if (left instanceof Iterable && right instanceof Iterable) {
            return listProperlyIncludes((Iterable) left, (Iterable) right);
        }

        throw new InvalidOperatorArgument(
                "ProperlyIncludes(Interval<T>, Interval<T>) or ProperlyIncludes(List<T>, List<T>)",
                String.format("ProperlyIncludes(%s, %s)", left.getClass().getName(), right.getClass().getName())
        );
    }

    public static Boolean intervalProperlyIncludes(Interval left, Interval right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        Object leftStart = left.getStart();
        Object leftEnd = left.getEnd();
        Object rightStart = right.getStart();
        Object rightEnd = right.getEnd();

        if (leftStart instanceof BaseTemporal || leftEnd instanceof BaseTemporal
                || rightStart instanceof BaseTemporal || rightEnd instanceof BaseTemporal) {
            Boolean isSame = AndEvaluator.and(
                    SameAsEvaluator.sameAs(leftStart, rightStart, precision),
                    SameAsEvaluator.sameAs(leftEnd, rightEnd, precision)
            );
            return AndEvaluator.and(
                    IncludedInEvaluator.intervalIncludedIn(right, left, precision),
                    isSame == null ? null : !isSame
            );
        }
        return AndEvaluator.and(
                IncludedInEvaluator.intervalIncludedIn(right, left, precision),
                NotEqualEvaluator.notEqual(left, right)
        );
    }

    public static Boolean listProperlyIncludes(Iterable left, Iterable right) {
        if (left == null) {
            return false;
        }

        int leftCount = (int) StreamSupport.stream(((Iterable<?>) left).spliterator(), false).count();

        if (right == null) {
            return leftCount > 0;
        }

        return AndEvaluator.and(
                IncludedInEvaluator.listIncludedIn(right, left),
                NotEqualEvaluator.notEqual(
                        leftCount,
                        (int) StreamSupport.stream(((Iterable<?>) right).spliterator(), false).count()
                )
        );
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() != null ? getPrecision().value() : null;

        return properlyIncludes(left, right, precision);
    }
}
