library TestPJMCQLIT266 version '0.0.006'


define function TestMessage(condition Boolean, testName String, expected String, result String):
  if condition
    then testName + ' TEST PASSED'
  else Message('TEST SUITE FAILED', true, 'Test ' + testName + ' failed', 'Warning', 'Expected ' + expected + ' Found ' + result)

define function toString(value Boolean):
  if value is null
    then 'null'
  else ToString(value)

define function toString(value Decimal):
  if value is null
    then 'null'
  else ToString(value)

define function toString(value Integer):
  if value is null
    then 'null'
  else ToString(value)

define function toString(value DateTime):
  if value is null
    then 'null'
  else ToString(value)

define function toString(value Date):
  if value is null
    then 'null'
  else ToString(value)

define function toString(value Time):
  if value is null
    then 'null'
  else ToString(value)

define function toString(value Quantity):
  if value is null
    then 'null'
  else ToString(value)

define function toString(value Code):
  if value is null
    then 'null'
  else 'Code { code: ' + value.code + ' system: ' + value.system + ' version: ' + value.version + ' display: ' + value.display + ' }'

define function toString(value Concept):
  if value is null
    then 'null'
  else 'Concept { ' + toString(value.codes) + ' display: ' + value.display + ' }'

define function toString(value Tuple{a Integer}):
  if value is null
    then 'null'
  else 'Tuple{ a: ' + ToString(value.a) + ' }'

define function toString(value Tuple{hello String}):
  if value is null
    then 'null'
  else 'Tuple{ hello: ' + value.hello + ' }'

define function toString(value Tuple{a Integer, b String}):
  if value is null
    then 'null'
  else 'Tuple{ a: ' + ToString(value.a) + ', b: ' + value.b + ' }'

define function toString(value Tuple{a Integer, b Integer, c Integer}):
  if value is null
    then 'null'
  else 'Tuple{ a: ' + ToString(value.a) + ', b: ' + ToString(value.b) + ', c: ' + ToString(value.c) + ' }'

define function toString(value List<Integer>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value Interval<Integer>):
  if value is null
    then 'null'
  else '[' + toString(start of value) + ', ' + toString(end of value) + ']'

define function toString(value Interval<Decimal>):
  if value is null
    then 'null'
  else '[' + toString(start of value) + ', ' + toString(end of value) + ']'

define function toString(value Interval<Quantity>):
  if value is null
    then 'null'
  else '[' + toString(start of value) + ', ' + toString(end of value) + ']'

define function toString(value Interval<DateTime>):
  if value is null
    then 'null'
  else '[' + toString(start of value) + ', ' + toString(end of value) + ']'

define function toString(value Interval<Date>):
  if value is null
    then 'null'
  else '[' + toString(start of value) + ', ' + toString(end of value) + ']'

define function toString(value Interval<Time>):
  if value is null
    then 'null'
  else '[' + toString(start of value) + ', ' + toString(end of value) + ']'

define function toString(value List<Code>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Interval<Integer>>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Interval<Decimal>>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Interval<Quantity>>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Interval<DateTime>>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Interval<Date>>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Interval<Time>>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Tuple{a Integer}>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Tuple{hello String}>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Tuple{a Integer, b String}>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

define function toString(value List<Tuple{a Integer, b Integer, c Integer}>):
  if value is null
    then 'null'
  else Combine((value V return toString(V)), ', ')

// Integer defaults to per 1
// Interval as Integer, closed
define "Question1x":
  expand { Interval [ 1, 5 ] }

define "Question2x":
  expand { Interval [ 1, 5 ) }

define "Question3x":
  expand { Interval [ 1.0, 5.0 ] }

define "Question3x2":
  expand { Interval [ 1.0, 5.0 ] } per 2.0

define "Question3x3":
  expand { Interval [ 1.0, 5.0 ] } per 0.1

// Interval as Decimal, open HI
define "Question4x":
  expand { Interval [ 1.0, 5.0 ) }

define "Question4x2":
  expand { Interval [ 1.0, 5.0 ) } per 2.0

define "Question4x3":
  expand { Interval [ 1.0, 5.0 ) } per 0.1

define "Question4x4":
  expand { Interval [ 1.00, 5.00 ) } per 0.1

define "Question4x5":
  expand { Interval [ 1.50, 5.50 ) } per 0.1

define "Question4x6":
  expand { Interval [ 1.55, 5.55 ) } per 0.1

// Interval as Decimal, open LO
define "Question5x":
  expand { Interval ( 1.0, 5.0] }

define "Question5x2":
  expand { Interval ( 1.0, 5.0] } per 2.0

define "Question5x3":
  expand { Interval ( 1.0, 5.0] } per 0.1

// Interval as Decimal, open LO HI
define "Question6x":
  expand { Interval ( 1.0, 5.0 ) }

define "Question6x1":
  expand { Interval ( 1.00, 5.00 ) }

define "Question6x2":
  expand { Interval ( 1.0, 5.0 ) } per 2.0

define "Question6x3":
  expand { Interval ( 1.0, 5.0 ) } per 0.1

define "Question6x4":
  expand { Interval ( 1.00, 5.00 ) } per 0.1

define "Question6x5":
  expand { Interval ( 1.50, 5.50 ) } per 0.1

define "Question6x6":
  expand { Interval ( 1.55, 5.55 ) } per 0.1

// Interval as Date
define "Question7x":
  expand { Interval[@2018-01-01, @2018-01-05]} per day

define "Question8x":
  expand { Interval[@2018-01-01T01:30:00, @2018-01-05T18:30:00]} per day

define "Question9xA":
  expand { Interval[@2018-01-01, @2018-01-31]} per day

define test_Question1x:
  TestMessage("Question1x" = List<Interval<Integer>>{Interval[1, 1], Interval[2, 2], Interval[3, 3], Interval[4, 4], Interval[5, 5]}, 'Question1x', toString(List<Interval<Integer>>{}), toString("Question1x"))

define test_Question2x:
  TestMessage("Question2x" = List<Interval<Integer>>{Interval[1, 1], Interval[2, 2], Interval[3, 3], Interval[4, 4]}, 'Question2x', toString(List<Interval<Integer>>{}), toString("Question2x"))

// Fails
//  [main] WARN org.opencds.cqf.cql.engine.elm.execution.MessageEvaluator - Test Question3x failed: Expected  Found [1.0, 1.99999999], [2.0, 2.99999999], [3.0, 3.99999999], [4.0, 4.999                                                                                        99999]
define test_Question3x:
  TestMessage("Question3x" = List<Interval<Integer>>{Interval[1, 1], Interval[2, 2], Interval[3, 3], Interval[4, 4], Interval[5, 5]}, 'Question3x', toString(List<Interval<Integer>>{}), toString("Question3x"))

// Fails
// [main] WARN org.opencds.cqf.cql.engine.elm.execution.MessageEvaluator - Test Question3x2 failed: Expected  Found [1.0, 2.99999999], [3.0, 4.99999999]
define test_Question3x2:
  TestMessage("Question3x2" = List<Interval<Integer>>{Interval[1, 2], Interval[3, 4]}, 'Question3x2', toString(List<Interval<Integer>>{}), toString("Question3x2"))

// Fails
// [main] WARN org.opencds.cqf.cql.engine.elm.execution.MessageEvaluator - Test Question4x failed: Expected  Found [1.0, 1.99999999], [2.0, 2.99999999], [3.0, 3.99999999], [4.0, 4.99999999]
define test_Question4x:
  TestMessage("Question4x" = List<Interval<Integer>>{Interval[1,1], Interval[2,2], Interval[3,3], Interval[4,4]}, 'Question4x', toString(List<Interval<Integer>>{}), toString("Question4x"))

// Fails
define test_Question4x2:
  TestMessage("Question4x2" = List<Interval<Decimal>>{Interval[1, 2], Interval[3, 4]}, 'Question4x2', toString(List<Interval<Decimal>>{}), toString("Question4x2"))

// Fails
define test_Question4x3:
  TestMessage("Question4x3" = List<Interval<Decimal>>{Interval[ 1,1.09999999], Interval[ 1.1,1.19999999], Interval[ 1.2,1.29999999], Interval[ 1.3,1.39999999], Interval[ 1.4,1.49999999], Interval[ 1.5,1.59999999], Interval[ 1.6,1.69999999], Interval[ 1.7,1.79999999], Interval[ 1.8,1.89999999], Interval[ 1.9,1.99999999], Interval[ 2,2.09999999], Interval[ 2.1,2.19999999], Interval[ 2.2,2.29999999], Interval[ 2.3,2.39999999], Interval[ 2.4,2.49999999], Interval[ 2.5,2.59999999], Interval[ 2.6,2.69999999], Interval[ 2.7,2.79999999], Interval[ 2.8,2.89999999], Interval[ 2.9,2.99999999], Interval[ 3,3.09999999], Interval[ 3.1,3.19999999], Interval[ 3.2,3.29999999], Interval[ 3.3,3.39999999], Interval[ 3.4,3.49999999], Interval[ 3.5,3.59999999], Interval[ 3.6,3.69999999], Interval[ 3.7,3.79999999], Interval[ 3.8,3.89999999], Interval[ 3.9, 3.99999999]}, 'Question4x3', toString(List<Interval<Decimal>>{}), toString("Question4x3"))

define test_Question4x4:
  TestMessage("Question4x4" = List<Interval<Decimal>>{Interval[1, 1.09999999], Interval[1.1, 1.19999999], Interval[1.2, 1.29999999], Interval[1.3, 1.39999999], Interval[1.4, 1.49999999], Interval[1.5, 1.59999999], Interval[1.6, 1.69999999], Interval[1.7, 1.79999999], Interval[1.8, 1.89999999], Interval[1.9, 1.99999999], Interval[2, 2.09999999], Interval[2.1, 2.19999999], Interval[2.2, 2.29999999], Interval[2.3, 2.39999999], Interval[2.4, 2.49999999], Interval[2.5, 2.59999999], Interval[2.6, 2.69999999], Interval[2.7, 2.79999999], Interval[2.8, 2.89999999], Interval[2.9, 2.99999999], Interval[3, 3.09999999], Interval[3.1, 3.19999999], Interval[3.2, 3.29999999], Interval[3.3, 3.39999999], Interval[3.4, 3.49999999], Interval[3.5, 3.59999999], Interval[3.6, 3.69999999], Interval[3.7, 3.79999999], Interval[3.8, 3.89999999], Interval[3.9, 3.99999999]}, 'Question4x4', toString(List<Interval<Decimal>>{}), toString("Question4x4"))

define test_Question4x5:
  TestMessage("Question4x5" = List<Interval<Decimal>>{Interval[ 1.5,1.59999999], Interval[ 1.6,1.69999999], Interval[ 1.7,1.79999999], Interval[ 1.8,1.89999999], Interval[ 1.9,1.99999999], Interval[ 2,2.09999999], Interval[ 2.1,2.19999999], Interval[ 2.2,2.29999999], Interval[ 2.3,2.39999999], Interval[ 2.4,2.49999999], Interval[ 2.5,2.59999999], Interval[ 2.6,2.69999999], Interval[ 2.7,2.79999999], Interval[ 2.8,2.89999999], Interval[ 2.9,2.99999999], Interval[ 3,3.09999999], Interval[ 3.1,3.19999999], Interval[ 3.2,3.29999999], Interval[ 3.3,3.39999999], Interval[ 3.4,3.49999999], Interval[ 3.5,3.59999999], Interval[ 3.6,3.69999999], Interval[ 3.7,3.79999999], Interval[ 3.8,3.89999999], Interval[ 3.9,3.99999999], Interval[ 4,4.09999999], Interval[ 4.1,4.19999999], Interval[ 4.2,4.29999999], Interval[ 4.3,4.39999999], Interval[ 4.4,4.49999999], Interval[ 4.5,4.59999999], Interval[ 4.6,4.69999999], Interval[ 4.7,4.79999999], Interval[ 4.8,4.89999999], Interval[ 4.9,4.99999999], Interval[ 5,5.09999999], Interval[ 5.1,5.19999999], Interval[ 5.2,5.29999999], Interval[ 5.3,5.39999999], Interval[5.4, 5.49999999]}, 'Question4x5', toString(List<Interval<Decimal>>{}), toString("Question4x5"))

define test_Question4x6:
  TestMessage("Question4x6" = List<Interval<Decimal>>{Interval[ 1.55,1.64999999], Interval[ 1.65,1.74999999], Interval[ 1.75,1.84999999], Interval[ 1.85,1.94999999], Interval[ 1.95,2.04999999], Interval[ 2.05,2.14999999], Interval[ 2.15,2.24999999], Interval[ 2.25,2.34999999], Interval[ 2.35,2.44999999], Interval[ 2.45,2.54999999], Interval[ 2.55,2.64999999], Interval[ 2.65,2.74999999], Interval[ 2.75,2.84999999], Interval[ 2.85,2.94999999], Interval[ 2.95,3.04999999], Interval[ 3.05,3.14999999], Interval[ 3.15,3.24999999], Interval[ 3.25,3.34999999], Interval[ 3.35,3.44999999], Interval[ 3.45,3.54999999], Interval[ 3.55,3.64999999], Interval[ 3.65,3.74999999], Interval[ 3.75,3.84999999], Interval[ 3.85,3.94999999], Interval[ 3.95,4.04999999], Interval[ 4.05,4.14999999], Interval[ 4.15,4.24999999], Interval[ 4.25,4.34999999], Interval[ 4.35,4.44999999], Interval[ 4.45,4.54999999], Interval[ 4.55,4.64999999], Interval[ 4.65,4.74999999], Interval[ 4.75,4.84999999], Interval[ 4.85,4.94999999], Interval[ 4.95,5.04999999], Interval[ 5.05,5.14999999], Interval[ 5.15,5.24999999], Interval[ 5.25,5.34999999], Interval[ 5.35,5.44999999], Interval[ 5.45,5.54999999]}, 'Question4x6', toString(List<Interval<Decimal>>{}), toString("Question4x6"))

// failed
define test_Question5x:
  TestMessage("Question5x" = List<Interval<Integer>>{Interval[ 2,2], Interval[ 3,3], Interval[ 4,4], Interval[ 5,5]}, 'Questionx5', toString(List<Interval<Integer>>{}), toString("Question5x"))

// failed
define test_Question5x2:
  TestMessage("Question5x2" = List<Interval<Integer>>{Interval[2,2], Interval[4,4], Interval[5,5]}, 'Question5x2', toString(List<Interval<Integer>>{}), toString("Question5x2"))

define test_Question5x3:
  TestMessage("Question5x3" = List<Interval<Decimal>>{Interval[ 2,2.09999999], Interval[ 2.1,2.19999999], Interval[ 2.2,2.29999999], Interval[ 2.3,2.39999999], Interval[ 2.4,2.49999999], Interval[ 2.5,2.59999999], Interval[ 2.6,2.69999999], Interval[ 2.7,2.79999999], Interval[ 2.8,2.89999999], Interval[ 2.9,2.99999999], Interval[ 3,3.09999999], Interval[ 3.1,3.19999999], Interval[ 3.2,3.29999999], Interval[ 3.3,3.39999999], Interval[ 3.4,3.49999999], Interval[ 3.5,3.59999999], Interval[ 3.6,3.69999999], Interval[ 3.7,3.79999999], Interval[ 3.8,3.89999999], Interval[ 3.9,3.99999999], Interval[ 4,4.09999999], Interval[ 4.1,4.19999999], Interval[ 4.2,4.29999999], Interval[ 4.3,4.39999999], Interval[ 4.4,4.49999999], Interval[ 4.5,4.59999999], Interval[ 4.6,4.69999999], Interval[ 4.7,4.79999999], Interval[ 4.8,4.89999999], Interval[ 4.9,4.99999999]}, 'Question5x3', toString(List<Interval<Decimal>>{}), toString("Question5x3"))

// failed
define test_Question6x:
  TestMessage("Question6x" = List<Interval<Decimal>>{Interval[2,2], Interval[3,3], Interval[4,4]}, 'Question6x', toString(List<Interval<Decimal>>{}), toString("Question6x"))

define test_Question6x2:
  TestMessage("Question6x2" = List<Interval<Decimal>>{Interval[ 2,2.09999999], Interval[ 2.1,2.19999999], Interval[ 2.2,2.29999999], Interval[ 2.3,2.39999999], Interval[ 2.4,2.49999999], Interval[ 2.5,2.59999999], Interval[ 2.6,2.69999999], Interval[ 2.7,2.79999999], Interval[ 2.8,2.89999999], Interval[ 2.9,2.99999999], Interval[ 3,3.09999999], Interval[ 3.1,3.19999999], Interval[ 3.2,3.29999999], Interval[ 3.3,3.39999999], Interval[ 3.4,3.49999999], Interval[ 3.5,3.59999999], Interval[ 3.6,3.69999999], Interval[ 3.7,3.79999999], Interval[ 3.8,3.89999999], Interval[ 3.9,3.99999999], Interval[ 4,4.09999999], Interval[ 4.1,4.19999999], Interval[ 4.2,4.29999999], Interval[ 4.3,4.39999999], Interval[ 4.4,4.49999999], Interval[ 4.5,4.59999999], Interval[ 4.6,4.69999999], Interval[ 4.7,4.79999999], Interval[ 4.8,4.89999999], Interval[ 4.9,4.99999999]}, 'Question6x2', toString(List<Interval<Decimal>>{}), toString("Question6x2"))
