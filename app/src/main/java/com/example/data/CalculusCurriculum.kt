package com.example.data

import com.example.model.*

object CalculusCurriculum {

    val parts: List<Part> = listOf(
        Part(
            id = 1,
            title = "Part 1 — Foundations",
            subtitle = "Before Calculus: Functions, Slopes & The Trouble with Curves",
            tabLabel = "Foundations",
            topicIds = listOf(1, 2, 3, 4)
        ),
        Part(
            id = 2,
            title = "Part 2 — Limits",
            subtitle = "The Microscope of Math: Approaching Without Touching",
            tabLabel = "Limits",
            topicIds = listOf(5, 6, 7, 8)
        ),
        Part(
            id = 3,
            title = "Part 3 — Differentiation",
            subtitle = "The Instantaneous Rate of Change: Slopes of Curves",
            tabLabel = "Derivatives",
            topicIds = listOf(9, 10, 11, 12, 13, 14, 15)
        ),
        Part(
            id = 4,
            title = "Part 4 — Integration",
            subtitle = "The Art of Accumulation: Areas, Volumes & The FTC",
            tabLabel = "Integrals",
            topicIds = listOf(16, 17, 18, 19, 20, 21)
        ),
        Part(
            id = 5,
            title = "Part 5 — Advanced Topics",
            subtitle = "Infinity & Dimensions: Series, Partials & Differential Equations",
            tabLabel = "Advanced",
            topicIds = listOf(22, 23, 24, 25)
        )
    )

    val topics: List<Topic> = listOf(
        // Topic 1
        Topic(
            id = 1,
            partId = 1,
            partTitle = "Part 1 — Foundations",
            chapterNumber = 1,
            title = "What is a Function & Why We Care About Change",
            shortTitle = "1. Functions & Change",
            plainEnglishIntuition = "Think of a function as an honest vending machine: you drop in one specific coin (the input x), and it always delivers one predictable snack (the output y = f(x)). The universe is never static — your coffee cools down, rockets burn fuel, bank accounts gather interest, and bacteria multiply. Calculus was invented because algebra only describes static snapshots, but nature is an ongoing movie of continuous transformation.",
            formulaTitle = "The Function Mapping Rule",
            formulaLatex = "y = f(x) \\quad \\text{where each input } x \\mapsto y",
            formulaExplanation = "f is the rule/machine name. x is the independent variable (input). f(x) is the dependent outcome (output). If one input could produce two outputs, predictability breaks!",
            diagramType = DiagramType.FUNCTION_MACHINE,
            diagramCaption = "The Input-Output Rule Machine: drop in x = 3, turn crank f(x) = 2x + 1, get out 7.",
            exampleTitle = "Evaluating and Graphing a Cost Function",
            exampleProblem = "A taxi charges a \$3.00 pickup fee plus \$2.00 per mile. Write the function C(m) and find the cost of a 7-mile ride.",
            exampleSteps = listOf(
                WorkedStep(1, "Identify the fixed start value and rate of change", "Base = 3, Rate = 2 per mile"),
                WorkedStep(2, "Construct the linear relationship function", "C(m) = 2m + 3"),
                WorkedStep(3, "Substitute the input value m = 7", "C(7) = 2(7) + 3"),
                WorkedStep(4, "Compute arithmetic to find final cost", "C(7) = 14 + 3 = 17 dollars")
            ),
            practiceProblem = PracticeProblem(
                prompt = "A baker's dough expands over hours according to V(t) = 3t^2 + 5. What is the volume at t = 4 hours?",
                hint = "Replace every t in the formula with (4) and remember to square before multiplying!",
                solutionSteps = listOf(
                    "V(4) = 3(4)^2 + 5",
                    "4 squared is 16: V(4) = 3(16) + 5",
                    "Multiply: 3 * 16 = 48",
                    "Add: 48 + 5 = 53"
                ),
                finalAnswer = "53 units of volume"
            ),
            commonMistake = "Don't confuse f(x) with multiplication! f(x) means 'f of x' (the function rule applied to x), NOT 'f times x'.",
            teacherTip = "Always ask yourself: 'What is changing with respect to what?' That is the foundation of all calculus."
        ),

        // Topic 2
        Topic(
            id = 2,
            partId = 1,
            partTitle = "Part 1 — Foundations",
            chapterNumber = 2,
            title = "Rate of Change in Everyday Life",
            shortTitle = "2. Rates of Change",
            plainEnglishIntuition = "When you drive 120 miles in 2 hours, your average speed was 60 mph. But were you driving at exactly 60 mph every single second? Of course not! You were stopped at red lights, stuck behind a truck at 25 mph, and cruising on the highway at 70 mph. Calculus exists to bridge the gulf between the 'average' over a long journey and what your speedometer reads at this exact split-second.",
            formulaTitle = "Average Rate of Change Formula",
            formulaLatex = "\\text{Average Rate} = \\frac{\\Delta y}{\\Delta x} = \\frac{f(b) - f(a)}{b - a}",
            formulaExplanation = "Δ (Greek delta) means 'change in'. We divide the total vertical shift (output change) by the total horizontal duration (input span).",
            diagramType = DiagramType.RATE_OF_CHANGE_GRAPH,
            diagramCaption = "Average vs Instantaneous: The straight chord shows 60 mph average, while the wiggly road had stops and sprints.",
            exampleTitle = "Finding Average Growth Rate of a Plant",
            exampleProblem = "A sunflower is 10 cm tall on day 2 and grows to 46 cm tall on day 8. What is its average daily growth rate?",
            exampleSteps = listOf(
                WorkedStep(1, "Define the coordinates (day, height)", "(a, f(a)) = (2, 10) and (b, f(b)) = (8, 46)"),
                WorkedStep(2, "Calculate change in height Δy", "Δy = 46 - 10 = 36 cm"),
                WorkedStep(3, "Calculate change in time Δx", "Δx = 8 - 2 = 6 days"),
                WorkedStep(4, "Divide Δy by Δx", "Rate = 36 / 6 = 6 cm/day")
            ),
            practiceProblem = PracticeProblem(
                prompt = "A drone's altitude is given by h(t) = 5t^2 meters. What is its average climb speed between t = 1 and t = 3 seconds?",
                hint = "Calculate h(1) and h(3), then compute [h(3) - h(1)] / (3 - 1).",
                solutionSteps = listOf(
                    "h(1) = 5(1)^2 = 5 meters",
                    "h(3) = 5(3)^2 = 5(9) = 45 meters",
                    "Δh = 45 - 5 = 40 meters",
                    "Δt = 3 - 1 = 2 seconds",
                    "Average speed = 40 / 2 = 20 m/s"
                ),
                finalAnswer = "20 m/s"
            ),
            commonMistake = "Mixing up the order in numerator vs denominator: if you do [f(b) - f(a)], the bottom must be [b - a], never [a - b]!",
            teacherTip = "Always include units in your answer: (unit of y) per (unit of x), like miles/hour or dollars/year."
        ),

        // Topic 3
        Topic(
            id = 3,
            partId = 1,
            partTitle = "Part 1 — Foundations",
            chapterNumber = 3,
            title = "Slope of a Straight Line (Algebra Recap)",
            shortTitle = "3. Line Slopes",
            plainEnglishIntuition = "A straight line is the simplest, cleanest geometric creature because its steepness never wavers. No matter where you stand on a ramp — at the bottom, middle, or top — every single step forward raises you by the exact same amount. That constant steepness is the slope m = Rise / Run. In calculus, straight lines will become our yardstick for measuring everything else.",
            formulaTitle = "Slope of a Straight Line",
            formulaLatex = "m = \\frac{y_2 - y_1}{x_2 - x_1} = \\frac{\\Delta y}{\\Delta x}",
            formulaExplanation = "Positive slope climbs left-to-right; negative slope descends; zero slope is completely flat; vertical lines have undefined slope (infinite steepness).",
            diagramType = DiagramType.SLOPE_TRIANGLE,
            diagramCaption = "The Classic Slope Triangle: Walk across by Run = Δx, step upward by Rise = Δy.",
            exampleTitle = "Finding Line Slope and Equation",
            exampleProblem = "Find the slope of the line passing through points P(1, 3) and Q(5, 11), and write its equation in y = mx + b form.",
            exampleSteps = listOf(
                WorkedStep(1, "Assign coordinates", "(x₁, y₁) = (1, 3), (x₂, y₂) = (5, 11)"),
                WorkedStep(2, "Calculate rise and run", "Rise = 11 - 3 = 8, Run = 5 - 1 = 4"),
                WorkedStep(3, "Calculate slope m", "m = 8 / 4 = 2"),
                WorkedStep(4, "Solve for y-intercept b using y = mx + b", "3 = 2(1) + b  =>  b = 1, so y = 2x + 1")
            ),
            practiceProblem = PracticeProblem(
                prompt = "A line connects A(-2, 7) and B(4, -5). What is its slope?",
                hint = "Watch negative signs carefully: (-5 - 7) divided by (4 - (-2)).",
                solutionSteps = listOf(
                    "Rise = -5 - 7 = -12",
                    "Run = 4 - (-2) = 4 + 2 = 6",
                    "m = -12 / 6 = -2"
                ),
                finalAnswer = "m = -2 (descending line)"
            ),
            commonMistake = "Putting run over rise (Δx / Δy)! Remember: you must RISE out of bed before you can RUN to school!",
            teacherTip = "If a line is horizontal, slope = 0. If vertical, slope is undefined (you can't divide by zero run!)."
        ),

        // Topic 4
        Topic(
            id = 4,
            partId = 1,
            partTitle = "Part 1 — Foundations",
            chapterNumber = 4,
            title = "The Trouble with Curves: Why Straight Lines Fail",
            shortTitle = "4. Trouble with Curves",
            plainEnglishIntuition = "Imagine riding a roller coaster or hiking up a parabola like y = x^2. At the bottom (x = 0), the track is flat as a pancake. At x = 1, it's a gentle hill. By x = 5, it's a near-vertical cliff! A curve does NOT have a single slope. If you pick two far-apart points, a straight line between them cuts across the curve and gives a wildly misleading average. How can we talk about the steepness of a curve at ONE solitary point?",
            formulaTitle = "The Secant Line Approximation Dilemma",
            formulaLatex = "m_{\\text{secant}} = \\frac{f(x + \\Delta x) - f(x)}{\\Delta x} \\quad (\\Delta x \\neq 0)",
            formulaExplanation = "If we pick two distinct points, Δx > 0 and we get a secant chord. But if we try to measure steepness at just ONE point, Δx = 0, giving 0/0 — the forbidden mathematical abyss!",
            diagramType = DiagramType.CURVE_VS_LINE,
            diagramCaption = "Secant Line cutting through a parabolic curve: it averages out the true local steepness.",
            exampleTitle = "Secant Line Slopes on y = x^2 Approaching a Point",
            exampleProblem = "For f(x) = x^2, calculate the secant line slope from x = 1 to x = 2, and then from x = 1 to x = 1.1.",
            exampleSteps = listOf(
                WorkedStep(1, "For interval [1, 2]: find points", "(1, 1) and (2, 4)"),
                WorkedStep(2, "Compute secant slope for [1, 2]", "m = (4 - 1) / (2 - 1) = 3 / 1 = 3"),
                WorkedStep(3, "For interval [1, 1.1]: find points", "(1, 1) and (1.1, 1.21)"),
                WorkedStep(4, "Compute closer secant slope", "m = (1.21 - 1) / (1.1 - 1) = 0.21 / 0.1 = 2.1"),
                WorkedStep(5, "Observe the pattern as interval shrinks", "Slopes: 3, 2.5, 2.1, 2.01... it appears to be heading towards 2!")
            ),
            practiceProblem = PracticeProblem(
                prompt = "For f(x) = x^2, calculate the secant slope from x = 1 to x = 1.01.",
                hint = "Find (1.01)^2 = 1.0201, then divide (1.0201 - 1) by (1.01 - 1).",
                solutionSteps = listOf(
                    "f(1) = 1",
                    "f(1.01) = (1.01)^2 = 1.0201",
                    "Δy = 1.0201 - 1 = 0.0201",
                    "Δx = 1.01 - 1 = 0.01",
                    "Slope = 0.0201 / 0.01 = 2.01"
                ),
                finalAnswer = "2.01 (remarkably close to 2!)"
            ),
            commonMistake = "Trying to set Δx = 0 directly. You get 0/0, which is undefined in standard algebra. That's why we must invent Limits!",
            teacherTip = "This 0/0 crisis was the brick wall mathematicians hit in the 1600s until Newton and Leibniz solved it with limits."
        ),

        // Topic 5
        Topic(
            id = 5,
            partId = 2,
            partTitle = "Part 2 — Limits",
            chapterNumber = 5,
            title = "What is a Limit? The Canyon Story",
            shortTitle = "5. Intuitive Limits",
            plainEnglishIntuition = "Imagine a rope bridge over a bottomless canyon has collapsed in the exact middle. You cannot step onto the missing plank without falling. But can you walk up to within 1 inch? Yes. Within a millimeter? Yes. Within a nanometer? Yes! A limit does NOT care what actually happens at the missing spot; it only cares about where you are headed as you walk infinitely close from both the left and the right sides.",
            formulaTitle = "The Intuitive Limit Concept",
            formulaLatex = "\\lim_{x \\to c} f(x) = L",
            formulaExplanation = "Read as: 'As x gets arbitrarily close to c (without actually needing to equal c), f(x) gets arbitrarily close to the target value L.'",
            diagramType = DiagramType.LIMIT_APPROACH,
            diagramCaption = "Two travelers approaching a hole in the road from left and right: both agree where the bridge should be!",
            exampleTitle = "Evaluating a Function with a Hole",
            exampleProblem = "Find lim_{x -> 2} (x^2 - 4) / (x - 2).",
            exampleSteps = listOf(
                WorkedStep(1, "Plug in x = 2 directly to check", "(2^2 - 4) / (2 - 2) = 0 / 0 (indeterminate!)"),
                WorkedStep(2, "Factor the numerator difference of squares", "x^2 - 4 = (x - 2)(x + 2)"),
                WorkedStep(3, "Cancel the hole factor (since x != 2 as we approach)", "[(x - 2)(x + 2)] / (x - 2) = x + 2"),
                WorkedStep(4, "Now evaluate the limit safely", "lim_{x -> 2} (x + 2) = 2 + 2 = 4")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Evaluate lim_{x -> 3} (x^2 - 9) / (x - 3).",
                hint = "Factor x^2 - 9 into (x - 3)(x + 3), cancel the (x - 3), then substitute x = 3.",
                solutionSteps = listOf(
                    "Direct substitution gives 0/0",
                    "Factor: (x - 3)(x + 3) / (x - 3)",
                    "Cancel (x - 3) for x ≠ 3 to leave (x + 3)",
                    "Substitute x = 3: 3 + 3 = 6"
                ),
                finalAnswer = "6"
            ),
            commonMistake = "Thinking that 0/0 means 'the limit does not exist' or 'the answer is 0'. 0/0 is a signal to do algebraic work (factor, conjugate, or simplify)!",
            teacherTip = "Whenever you see 0/0, tell yourself: 'There is a treasure hidden here, I just have to simplify first.'"
        ),

        // Topic 6
        Topic(
            id = 6,
            partId = 2,
            partTitle = "Part 2 — Limits",
            chapterNumber = 6,
            title = "Limits from Graphs: Holes, Jumps & Asymptotes",
            shortTitle = "6. Limits from Graphs",
            plainEnglishIntuition = "A graph is a visual roadmap of a function. As your pencil traces the curve toward an x-coordinate c from the left side (x -> c^-) and from the right side (x -> c^+), do both pencil tips arrive at the exact same altitude? If they do, the two-sided limit exists! If one pencil tip lands at y = 2 and the other at y = 5 (a jump), they disagree, so the overall limit Does Not Exist (DNE).",
            formulaTitle = "One-Sided Limits Agreement Rule",
            formulaLatex = "\\lim_{x \\to c} f(x) = L \\iff \\lim_{x \\to c^-} f(x) = \\lim_{x \\to c^+} f(x) = L",
            formulaExplanation = "c⁻ means approaching from values smaller than c (from the left). c⁺ means approaching from values larger than c (from the right).",
            diagramType = DiagramType.LIMIT_HOLE_GRAPH,
            diagramCaption = "Hole vs Jump: At a removable hole, left and right limits match! At a cliff jump, they disagree (DNE).",
            exampleTitle = "Reading Limits from a Piecewise Step Graph",
            exampleProblem = "Given a piecewise function where f(x) = x + 1 for x < 2, and f(x) = 6 - x for x > 2, with f(2) = 10. Does lim_{x -> 2} f(x) exist?",
            exampleSteps = listOf(
                WorkedStep(1, "Calculate left-hand limit x -> 2⁻", "lim_{x -> 2⁻} (x + 1) = 2 + 1 = 3"),
                WorkedStep(2, "Calculate right-hand limit x -> 2⁺", "lim_{x -> 2⁺} (6 - x) = 6 - 2 = 3"),
                WorkedStep(3, "Compare left and right limits", "3 == 3, both sides agree!"),
                WorkedStep(4, "Conclude overall limit", "lim_{x -> 2} f(x) = 3 (even though the actual point f(2) = 10 is plotted up in the sky!)")
            ),
            practiceProblem = PracticeProblem(
                prompt = "If a function approaches 5 from the left as x -> 1, but approaches -2 from the right as x -> 1, what is lim_{x -> 1} f(x)?",
                hint = "Do the left and right values match?",
                solutionSteps = listOf(
                    "Left limit = 5",
                    "Right limit = -2",
                    "Because 5 ≠ -2, the two sides do not meet",
                    "Therefore the two-sided limit Does Not Exist"
                ),
                finalAnswer = "Does Not Exist (DNE)"
            ),
            commonMistake = "Looking at the isolated dot f(c) instead of where the curves are aiming. The limit cares about the journey, not the dot!",
            teacherTip = "Put two fingers on the curve on opposite sides of c and slide them toward c. If your fingertips touch, the limit exists."
        ),

        // Topic 7
        Topic(
            id = 7,
            partId = 2,
            partTitle = "Part 2 — Limits",
            chapterNumber = 7,
            title = "Formal Notation & The Limit Laws",
            shortTitle = "7. Limit Laws",
            plainEnglishIntuition = "Limits behave like well-mannered arithmetic operations: the limit of a sum is simply the sum of the limits, and the limit of a product is the product of the limits. Because of this, you can break complicated algebraic beasts into gentle bite-sized chunks. For polite polynomials and fractions where the denominator doesn't vanish, direct substitution is all you need!",
            formulaTitle = "Core Limit Laws",
            formulaLatex = "\\lim_{x \\to c} [f(x) \\pm g(x)] = \\lim_{x \\to c} f(x) \\pm \\lim_{x \\to c} g(x)",
            formulaExplanation = "For quotients: lim [f(x)/g(x)] = [lim f(x)] / [lim g(x)] provided the denominator limit is not 0.",
            diagramType = DiagramType.LIMIT_LAWS_CHART,
            diagramCaption = "The Friendly Limit Toolbox: You can push limits inside sums, products, powers, and roots.",
            exampleTitle = "Using Limit Laws to Deconstruct an Expression",
            exampleProblem = "Evaluate lim_{x -> 4} [3x^2 - 5x + 2].",
            exampleSteps = listOf(
                WorkedStep(1, "Apply sum and difference laws", "lim(3x^2) - lim(5x) + lim(2)"),
                WorkedStep(2, "Pull constants out in front", "3·lim(x^2) - 5·lim(x) + 2"),
                WorkedStep(3, "Evaluate individual basic limits", "3·(4^2) - 5·(4) + 2"),
                WorkedStep(4, "Combine arithmetic", "3(16) - 20 + 2 = 48 - 20 + 2 = 30")
            ),
            practiceProblem = PracticeProblem(
                prompt = "If lim_{x -> 2} f(x) = 7 and lim_{x -> 2} g(x) = 3, what is lim_{x -> 2} [2f(x) - g(x)^2]?",
                hint = "Substitute 7 for f(x) and 3 for g(x): 2(7) - (3)^2.",
                solutionSteps = listOf(
                    "2 * lim f(x) = 2 * 7 = 14",
                    "[lim g(x)]^2 = 3^2 = 9",
                    "14 - 9 = 5"
                ),
                finalAnswer = "5"
            ),
            commonMistake = "Applying the quotient law when the bottom limit is 0 without simplifying first!",
            teacherTip = "Always try direct substitution first. If you get a clean number, you are done! If you get 0/0, factor and cancel."
        ),

        // Topic 8
        Topic(
            id = 8,
            partId = 2,
            partTitle = "Part 2 — Limits",
            chapterNumber = 8,
            title = "Continuity: What 'No Jumps' Really Means",
            shortTitle = "8. Continuity",
            plainEnglishIntuition = "Informally, a curve is continuous if you can trace it from start to finish without lifting your pen from the paper. Formally, continuity is the perfect marriage of three conditions: the point must exist, the limit must exist as you approach it, and the limit must match the point exactly! Continuity guarantees there are no surprise teleportations or missing pixels.",
            formulaTitle = "The Three-Part Test for Continuity at x = c",
            formulaLatex = "1)\\; f(c) \\text{ defined} \\quad 2)\\; \\lim_{x \\to c} f(x) \\text{ exists} \\quad 3)\\; \\lim_{x \\to c} f(x) = f(c)",
            formulaExplanation = "If any single one of these three fails, the function suffers a discontinuity (removable hole, jump, or infinite asymptote).",
            diagramType = DiagramType.CONTINUITY_TEST,
            diagramCaption = "Smooth unbroken road: The destination point matches where both lanes are heading.",
            exampleTitle = "Testing Continuity of a Piecewise Function",
            exampleProblem = "Is f(x) continuous at x = 3 if f(x) = (x^2 - 9)/(x - 3) for x != 3 and f(3) = 6?",
            exampleSteps = listOf(
                WorkedStep(1, "Check condition 1: is f(3) defined?", "Yes, f(3) = 6 is explicitly given."),
                WorkedStep(2, "Check condition 2: does lim_{x -> 3} f(x) exist?", "Factor: (x-3)(x+3)/(x-3) = x+3. Limit as x -> 3 is 3+3 = 6. Yes, exists!"),
                WorkedStep(3, "Check condition 3: does limit equal value?", "lim f(x) = 6 and f(3) = 6. They match!"),
                WorkedStep(4, "Conclusion", "f(x) is completely continuous at x = 3.")
            ),
            practiceProblem = PracticeProblem(
                prompt = "A function has f(1) = 4, but lim_{x -> 1} f(x) = 7. Is f(x) continuous at x = 1?",
                hint = "Compare the limit value with the function output f(1).",
                solutionSteps = listOf(
                    "f(1) is defined (= 4)",
                    "lim_{x -> 1} f(x) exists (= 7)",
                    "However, 7 ≠ 4 (the limit does not equal f(1))",
                    "There is a hole with an offset dot"
                ),
                finalAnswer = "No, discontinuous (removable discontinuity)"
            ),
            commonMistake = "Assuming that having a defined point f(c) automatically makes it continuous. The limit must also equal that point!",
            teacherTip = "The Intermediate Value Theorem (IVT) says if a continuous curve goes from negative to positive, it MUST cross zero. No teleporting!"
        ),

        // Topic 9
        Topic(
            id = 9,
            partId = 3,
            partTitle = "Part 3 — Differentiation",
            chapterNumber = 9,
            title = "The Problem of Instantaneous Rate of Change",
            shortTitle = "9. Instantaneous Rate",
            plainEnglishIntuition = "If a traffic camera snaps your photo at a specific microsecond, how does it know you were speeding? You only traveled a fraction of a millimeter during the camera shutter! Speed requires distance divided by time (s = d/t). But at one single instant, time elapsed is 0, and distance covered is 0, yielding 0/0. The derivative is mathematics' genius trick for discovering your speed RIGHT NOW.",
            formulaTitle = "Instantaneous Velocity as a Limit",
            formulaLatex = "v(t) = \\lim_{\\Delta t \\to 0} \\frac{s(t + \\Delta t) - s(t)}{\\Delta t}",
            formulaExplanation = "We shrink the time window Δt closer and closer to zero. As Δt vanishes, the average speed converges to the instantaneous speedometer reading!",
            diagramType = DiagramType.INSTANTANEOUS_SPEED,
            diagramCaption = "Shrinking the stopwatch: From 10 seconds, to 1 second, to 0.001 second, approaching the truth.",
            exampleTitle = "Approximating Speed of a Dropped Ball",
            exampleProblem = "A ball drops with position s(t) = 16t^2 feet. Find its average speed between t = 2 and t = 2.001 s, and predict its instant speed at t = 2.",
            exampleSteps = listOf(
                WorkedStep(1, "Compute position at t = 2", "s(2) = 16(4) = 64 feet"),
                WorkedStep(2, "Compute position at t = 2.001", "s(2.001) = 16(4.004001) = 64.064016 feet"),
                WorkedStep(3, "Calculate Δs / Δt", "(64.064016 - 64) / 0.001 = 0.064016 / 0.001 = 64.016 ft/s"),
                WorkedStep(4, "Predict exact instantaneous speed at t = 2", "As Δt -> 0, the decimal part vanishes: exactly 64 ft/s!")
            ),
            practiceProblem = PracticeProblem(
                prompt = "If s(t) = 10t^2, what is the instantaneous speed at t = 3 seconds?",
                hint = "Use [10(3 + h)^2 - 10(9)] / h and let h -> 0.",
                solutionSteps = listOf(
                    "Expand: 10(9 + 6h + h^2) - 90 = 90 + 60h + 10h^2 - 90 = 60h + 10h^2",
                    "Divide by h: (60h + 10h^2)/h = 60 + 10h",
                    "Let h -> 0: 60 + 0 = 60"
                ),
                finalAnswer = "60 units/second"
            ),
            commonMistake = "Thinking instantaneous speed means 'speed when time is zero'. It means speed at a single moment of time t!",
            teacherTip = "The derivative is the speedometer of calculus. Whenever you hear 'rate of change', think DERIVATIVE."
        ),

        // Topic 10
        Topic(
            id = 10,
            partId = 3,
            partTitle = "Part 3 — Differentiation",
            chapterNumber = 10,
            title = "Deriving the Derivative from First Principles",
            shortTitle = "10. First Principles",
            plainEnglishIntuition = "Here it is: the Holy Grail of Calculus. We place two points on a curve: (x, f(x)) and a neighboring point (x + h, f(x + h)). We draw the secant line between them. Then we turn the knob on h and squeeze it down toward zero. The second point slides down the curve until it merges with the first point. The secant line becomes the tangent line, and algebra transforms into the derivative f'(x)!",
            formulaTitle = "The Limit Definition of the Derivative",
            formulaLatex = "f'(x) = \\lim_{h \\to 0} \\frac{f(x + h) - f(x)}{h}",
            formulaExplanation = "h is the horizontal run between the two points. We do algebra to cancel the denominator h, then evaluate the limit h -> 0.",
            diagramType = DiagramType.FIRST_PRINCIPLES_LIMIT,
            diagramCaption = "Point Q sliding toward P as run h -> 0. The secant line snaps into the tangent line!",
            exampleTitle = "Differentiating f(x) = x^2 from First Principles",
            exampleProblem = "Use the limit definition of the derivative to find f'(x) for f(x) = x^2.",
            exampleSteps = listOf(
                WorkedStep(1, "Set up the limit definition", "lim_{h -> 0} [(x + h)^2 - x^2] / h"),
                WorkedStep(2, "Expand the binomial (x + h)^2", "lim_{h -> 0} [x^2 + 2xh + h^2 - x^2] / h"),
                WorkedStep(3, "Cancel x^2 - x^2 in numerator", "lim_{h -> 0} [2xh + h^2] / h"),
                WorkedStep(4, "Factor out h from numerator", "lim_{h -> 0} h(2x + h) / h"),
                WorkedStep(5, "Cancel h/h and let h -> 0", "lim_{h -> 0} (2x + h) = 2x + 0 = 2x!"),
                WorkedStep(6, "Result", "The derivative of x^2 is f'(x) = 2x.")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Use the limit definition to find the derivative of f(x) = 3x + 5.",
                hint = "f(x + h) = 3(x + h) + 5. Subtract (3x + 5) and divide by h.",
                solutionSteps = listOf(
                    "f(x + h) - f(x) = [3(x + h) + 5] - [3x + 5]",
                    "= 3x + 3h + 5 - 3x - 5 = 3h",
                    "Divide by h: 3h / h = 3",
                    "lim_{h -> 0} (3) = 3"
                ),
                finalAnswer = "f'(x) = 3 (constant slope of line!)"
            ),
            commonMistake = "Forgetting to distribute the negative sign when subtracting f(x): - (3x + 5) = -3x - 5, not -3x + 5!",
            teacherTip = "If h doesn't completely cancel out of the denominator before you plug in h = 0, check your algebra!"
        ),

        // Topic 11
        Topic(
            id = 11,
            partId = 3,
            partTitle = "Part 3 — Differentiation",
            chapterNumber = 11,
            title = "The Derivative as the Slope of the Tangent Line",
            shortTitle = "11. Tangent Lines",
            plainEnglishIntuition = "A tangent line is a straight ruler laid gently against a curved surface so it grazes the curve at exactly one touchpoint without slicing through it. If you were a tiny ant walking along the graph of f(x) and you suddenly slipped off on ice, you would fly off along the tangent line in the direction you were heading! The derivative f'(a) is simply the numerical slope of that grazing line.",
            formulaTitle = "Point-Slope Equation of the Tangent Line",
            formulaLatex = "y - f(a) = f'(a) \\cdot (x - a)",
            formulaExplanation = "a is the touchpoint x-value. f(a) is the touchpoint y-value. f'(a) is the steepness (slope m).",
            diagramType = DiagramType.INTERACTIVE_TANGENT,
            diagramCaption = "Interactive Tangent Explorer: Drag the touchpoint and watch the slope number tilt live!",
            exampleTitle = "Writing the Tangent Line to y = x^2 at x = 3",
            exampleProblem = "Find the equation of the line tangent to f(x) = x^2 at the point where x = 3.",
            exampleSteps = listOf(
                WorkedStep(1, "Find the y-coordinate of the touchpoint", "f(3) = 3^2 = 9. Point is (3, 9)."),
                WorkedStep(2, "Find the derivative formula", "f'(x) = 2x"),
                WorkedStep(3, "Plug in x = 3 to find slope m", "m = f'(3) = 2(3) = 6"),
                WorkedStep(4, "Write point-slope equation", "y - 9 = 6(x - 3)"),
                WorkedStep(5, "Simplify to y = mx + b form", "y - 9 = 6x - 18  =>  y = 6x - 9")
            ),
            practiceProblem = PracticeProblem(
                prompt = "For f(x) = x^2, find the slope of the tangent line at x = -4.",
                hint = "Since f'(x) = 2x, plug in x = -4.",
                solutionSteps = listOf(
                    "f'(x) = 2x",
                    "m = f'(-4) = 2(-4) = -8",
                    "Negative slope means the tangent line tilts downwards!"
                ),
                finalAnswer = "m = -8"
            ),
            commonMistake = "Plugging x = 3 into the derivative before finding the general derivative f'(x). Always differentiate first, then substitute numbers!",
            teacherTip = "At peaks and valleys, the tangent line is completely flat horizontal, meaning f'(x) = 0. This is how we find maxima and minima!"
        ),

        // Topic 12
        Topic(
            id = 12,
            partId = 3,
            partTitle = "Part 3 — Differentiation",
            chapterNumber = 12,
            title = "Basic Derivative Shortcut Rules",
            shortTitle = "12. Power & Sum Rules",
            plainEnglishIntuition = "Doing first-principles limits for every function takes pages of algebra. Fortunately, mathematicians proved universal shortcuts! The Power Rule is the king of shortcuts: to differentiate x^n, just bring the power n down to the front as a multiplier, and subtract 1 from the exponent. Constants differentiate to 0 because flat numbers never change.",
            formulaTitle = "The Power, Constant & Sum Rules",
            formulaLatex = "\\frac{d}{dx}[x^n] = n x^{n-1} \\quad \\Big| \\quad \\frac{d}{dx}[c] = 0 \\quad \\Big| \\quad \\frac{d}{dx}[f \\pm g] = f' \\pm g'",
            formulaExplanation = "Power rule works for any real power: positive, negative, and fractions (roots like √x = x^(1/2))!",
            diagramType = DiagramType.POWER_RULE_VISUAL,
            diagramCaption = "Power Rule in action: Bring the power down like an avalanche, then reduce the power by 1!",
            exampleTitle = "Differentiating a Multi-Term Polynomial",
            exampleProblem = "Find the derivative of f(x) = 5x^4 - 3x^2 + 7x - 9.",
            exampleSteps = listOf(
                WorkedStep(1, "Differentiate 5x^4 using power rule", "5 · (4x^3) = 20x^3"),
                WorkedStep(2, "Differentiate -3x^2 using power rule", "-3 · (2x^1) = -6x"),
                WorkedStep(3, "Differentiate 7x (which is 7x^1)", "7 · (1x^0) = 7(1) = 7"),
                WorkedStep(4, "Differentiate constant -9", "0 (flat numbers don't change!)"),
                WorkedStep(5, "Combine terms", "f'(x) = 20x^3 - 6x + 7")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Differentiate f(x) = x^5 + 4x^3 - 8.",
                hint = "Bring down powers 5 and 3, reduce exponents by 1.",
                solutionSteps = listOf(
                    "d/dx [x^5] = 5x^4",
                    "d/dx [4x^3] = 4 * 3x^2 = 12x^2",
                    "d/dx [-8] = 0",
                    "f'(x) = 5x^4 + 12x^2"
                ),
                finalAnswer = "f'(x) = 5x^4 + 12x^2"
            ),
            commonMistake = "Thinking the derivative of 7x is 7x or 0. The derivative of x is 1, so d/dx[7x] = 7!",
            teacherTip = "To differentiate √x, rewrite it as x^(1/2). Then (1/2)x^(-1/2) = 1 / (2√x). Beautiful!"
        ),

        // Topic 13
        Topic(
            id = 13,
            partId = 3,
            partTitle = "Part 3 — Differentiation",
            chapterNumber = 13,
            title = "Product, Quotient & Chain Rules",
            shortTitle = "13. Product & Chain Rules",
            plainEnglishIntuition = "When functions multiply, divide, or nest inside one another, you can't just differentiate piece by piece. Think of the Chain Rule as nested Russian Matryoshka dolls: to differentiate the outer doll, leave the inner doll tucked safely inside, then multiply by the derivative of the inner doll! For products, think of two dancers taking turns: First times derivative of Second, plus Second times derivative of First.",
            formulaTitle = "The Big Three Combination Rules",
            formulaLatex = "(f \\cdot g)' = f'g + fg' \\quad \\Big| \\quad [f(g(x))]' = f'(g(x)) \\cdot g'(x)",
            formulaExplanation = "Quotient: (u/v)' = (v·u' - u·v') / v^2 (rhyme: 'Low d-High minus High d-Low, over the square of what's Below').",
            diagramType = DiagramType.PRODUCT_CHAIN_RULE,
            diagramCaption = "Nested Gears (Chain Rule): When gear g turns at rate g', it drives gear f at rate f' times g'!",
            exampleTitle = "Applying the Chain Rule to a Composite Function",
            exampleProblem = "Find the derivative of h(x) = (3x^2 + 5)^4.",
            exampleSteps = listOf(
                WorkedStep(1, "Identify outer function and inner function", "Outer = ( )^4, Inner g(x) = 3x^2 + 5"),
                WorkedStep(2, "Differentiate outer function, keep inner untouched", "4(3x^2 + 5)^3"),
                WorkedStep(3, "Differentiate inner function g'(x)", "d/dx [3x^2 + 5] = 6x"),
                WorkedStep(4, "Multiply outer derivative by inner derivative", "h'(x) = 4(3x^2 + 5)^3 · (6x)"),
                WorkedStep(5, "Simplify front coefficients", "h'(x) = 24x(3x^2 + 5)^3")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Use the Product Rule to differentiate f(x) = x^3 · (2x + 1).",
                hint = "f' = (3x^2)(2x + 1) + (x^3)(2).",
                solutionSteps = listOf(
                    "Let u = x^3  =>  u' = 3x^2",
                    "Let v = 2x + 1  =>  v' = 2",
                    "Product rule: u'v + uv' = (3x^2)(2x + 1) + (x^3)(2)",
                    "Expand: 6x^3 + 3x^2 + 2x^3 = 8x^3 + 3x^2"
                ),
                finalAnswer = "8x^3 + 3x^2"
            ),
            commonMistake = "Forgetting to multiply by the inner derivative in the Chain Rule! (Leaving off the 6x in the example above is the #1 student error on calculus exams).",
            teacherTip = "Say the mantra aloud: 'Derivative of the outside, LEAVE THE INSIDE ALONE, times derivative of the inside.'"
        ),

        // Topic 14
        Topic(
            id = 14,
            partId = 3,
            partTitle = "Part 3 — Differentiation",
            chapterNumber = 14,
            title = "Derivatives of Trig, Exponential & Log Functions",
            shortTitle = "14. Trig, Exp & Logs",
            plainEnglishIntuition = "Nature loves e^x because e^x is the ONLY function that equals its own rate of change! If you have e^x bacteria, they produce new bacteria at rate e^x. Sine and cosine are cosmic dance partners: the slope of sin(x) perfectly graphs cos(x), and the slope of cos(x) graphs -sin(x). Differentiating them four times brings you right back where you started!",
            formulaTitle = "Transcendental Derivative Formulas",
            formulaLatex = "\\frac{d}{dx}[e^x] = e^x \\quad \\Big| \\quad \\frac{d}{dx}[\\ln x] = \\frac{1}{x} \\quad \\Big| \\quad \\frac{d}{dx}[\\sin x] = \\cos x",
            formulaExplanation = "For tangent: d/dx [tan x] = sec²(x). With Chain rule: d/dx [e^(g(x))] = e^(g(x)) · g'(x).",
            diagramType = DiagramType.TRIG_EXP_SKETCH,
            diagramCaption = "The Trig Derivative Cycle: sin -> cos -> -sin -> -cos -> back to sin!",
            exampleTitle = "Differentiating a Trig Composite Function",
            exampleProblem = "Find the derivative of y = sin(5x^2).",
            exampleSteps = listOf(
                WorkedStep(1, "Identify outer and inner functions", "Outer = sin(u), Inner u = 5x^2"),
                WorkedStep(2, "Differentiate outer: d/du[sin u] = cos(u)", "cos(5x^2)"),
                WorkedStep(3, "Differentiate inner: d/dx[5x^2] = 10x", "10x"),
                WorkedStep(4, "Multiply together by Chain Rule", "dy/dx = 10x · cos(5x^2)")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Find the derivative of f(x) = e^(3x).",
                hint = "d/dx[e^u] = e^u * u'. Here u = 3x, so u' = 3.",
                solutionSteps = listOf(
                    "Outer derivative = e^(3x)",
                    "Inner derivative = 3",
                    "f'(x) = 3e^(3x)"
                ),
                finalAnswer = "f'(x) = 3e^(3x)"
            ),
            commonMistake = "Writing d/dx [cos x] = sin x without the minus sign! Remember: all trig functions starting with 'co' (cos, cot, csc) have NEGATIVE derivatives!",
            teacherTip = "The 'Co-Rule': Differentiating Cosine, Cotangent, or Cosecant always produces a negative sign."
        ),

        // Topic 15
        Topic(
            id = 15,
            partId = 3,
            partTitle = "Part 3 — Differentiation",
            chapterNumber = 15,
            title = "Applications: Optimization & Related Rates",
            shortTitle = "15. Optimization & Rates",
            plainEnglishIntuition = "Calculus doesn't stay on paper — it designs aircraft wings, maximizes company profits, and tracks sliding ladders. When a curve reaches its highest peak (maximum profit) or lowest trough (minimum fuel burn), the slope flattens out completely: f'(x) = 0! By setting the derivative equal to zero, we unlock optimal solutions to real-world engineering puzzles.",
            formulaTitle = "Critical Point Condition for Max/Min",
            formulaLatex = "f'(x) = 0 \\quad \\text{or} \\quad f'(x) \\text{ is undefined}",
            formulaExplanation = "Use the First Derivative Test (sign change from + to - is a peak) or Second Derivative Test (f'' < 0 is concave down like a frown = peak!).",
            diagramType = DiagramType.LADDER_PROBLEM,
            diagramCaption = "The Classic Sliding Ladder: As the base pulls away at dx/dt, how fast does the top slide down?",
            exampleTitle = "Maximizing the Area of a Rectangular Garden",
            exampleProblem = "A farmer has 100 meters of fencing to enclose a rectangular pasture against a straight river (no fence needed along the river). What dimensions maximize area?",
            exampleSteps = listOf(
                WorkedStep(1, "Assign variables and perimeter equation", "Let width be x and length along river be y. Fence used: 2x + y = 100  =>  y = 100 - 2x"),
                WorkedStep(2, "Write Area function in terms of x only", "Area A(x) = x · y = x(100 - 2x) = 100x - 2x^2"),
                WorkedStep(3, "Differentiate A(x) with respect to x", "A'(x) = 100 - 4x"),
                WorkedStep(4, "Set derivative to 0 to find maximum", "100 - 4x = 0  =>  4x = 100  =>  x = 25 meters"),
                WorkedStep(5, "Find length y and maximum area", "y = 100 - 2(25) = 50 meters. Max Area = 25 * 50 = 1250 m^2!")
            ),
            practiceProblem = PracticeProblem(
                prompt = "A 10-foot ladder leans against a wall: x^2 + y^2 = 100. If the bottom is x = 6 ft away and sliding out at dx/dt = 2 ft/s, how fast is the top sliding down (dy/dt)?",
                hint = "Differentiate with respect to time t: 2x(dx/dt) + 2y(dy/dt) = 0. Note that when x = 6, y = 8.",
                solutionSteps = listOf(
                    "Differentiate: 2x(dx/dt) + 2y(dy/dt) = 0",
                    "Simplify: x(dx/dt) + y(dy/dt) = 0",
                    "Plug in x = 6, dx/dt = 2, and y = 8:",
                    "6(2) + 8(dy/dt) = 0  =>  12 + 8(dy/dt) = 0",
                    "dy/dt = -12 / 8 = -1.5 ft/s"
                ),
                finalAnswer = "-1.5 ft/s (sliding downward)"
            ),
            commonMistake = "Forgetting to verify whether your critical point is a maximum or a minimum! Always check with f''(x) or the first derivative sign line.",
            teacherTip = "In Related Rates problems, always draw a clear sketch first, label your variables, and write down what you know vs what you need."
        ),

        // Topic 16
        Topic(
            id = 16,
            partId = 4,
            partTitle = "Part 4 — Integration",
            chapterNumber = 16,
            title = "The Problem of Finding Area Under a Curve",
            shortTitle = "16. Area Under Curves",
            plainEnglishIntuition = "Finding the area of a rectangle is second nature: length × width. Triangles are easy too: 1/2 base × height. But what about the space underneath a parabolic arch, a sine wave, or an airplane wing? Straight geometric formulas are powerless against curved boundaries. Integration solves this by packing thousands of infinitesimally thin rectangles underneath the curve.",
            formulaTitle = "The Curved Area Problem",
            formulaLatex = "\\text{Area} = \\lim_{n \\to \\infty} \\sum_{i=1}^n f(x_i^*) \\Delta x",
            formulaExplanation = "Each slice has width Δx and height f(xᵢ*). As we slice thinner and thinner (n -> ∞), the jagged staircase of rectangles becomes the perfectly smooth curved area!",
            diagramType = DiagramType.AREA_PROBLEM,
            diagramCaption = "Curved boundary problem: How do we measure the exact area trapped between f(x) and the x-axis?",
            exampleTitle = "Estimating Area Under y = x^2 from x = 0 to x = 2 with 2 Rectangles",
            exampleProblem = "Estimate the area under f(x) = x^2 on [0, 2] using 2 right-endpoint rectangles.",
            exampleSteps = listOf(
                WorkedStep(1, "Calculate rectangle width Δx", "Δx = (b - a) / n = (2 - 0) / 2 = 1"),
                WorkedStep(2, "Identify right endpoints x₁ and x₂", "x₁ = 1, x₂ = 2"),
                WorkedStep(3, "Calculate heights f(1) and f(2)", "f(1) = 1^2 = 1, f(2) = 2^2 = 4"),
                WorkedStep(4, "Sum rectangle areas: (width)(height₁ + height₂)", "Area ≈ 1·(1) + 1·(4) = 1 + 4 = 5 (an overestimate!)")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Estimate the area under f(x) = 2x on [0, 4] using 2 midpoint rectangles.",
                hint = "Δx = (4 - 0)/2 = 2. Midpoints are x = 1 and x = 3. Find 2·[f(1) + f(3)].",
                solutionSteps = listOf(
                    "Width Δx = 2",
                    "Midpoint 1 = 1 => f(1) = 2(1) = 2",
                    "Midpoint 2 = 3 => f(3) = 2(3) = 6",
                    "Area ≈ 2 * (2 + 6) = 2 * 8 = 16"
                ),
                finalAnswer = "16 (exact triangle area: 1/2 * 4 * 8 = 16!)"
            ),
            commonMistake = "Confusing rectangle width Δx with the endpoints. Always compute Δx = (b - a) / n first!",
            teacherTip = "Integration is the opposite side of the calculus coin: differentiation breaks things down, integration adds them back up."
        ),

        // Topic 17
        Topic(
            id = 17,
            partId = 4,
            partTitle = "Part 4 — Integration",
            chapterNumber = 17,
            title = "Riemann Sums: Approximating with Rectangles",
            shortTitle = "17. Riemann Sums",
            plainEnglishIntuition = "Bernhard Riemann gave us the official recipe: divide the interval into n equal strips, erect a rectangle on each strip using its left edge, right edge, or midpoint, and add up all their areas. With 4 rectangles, the estimate is crude and bumpy. With 20 rectangles, the jagged gaps shrink to slivers. With 100 rectangles, it is almost indistinguishable from the exact area!",
            formulaTitle = "Riemann Sum Formula",
            formulaLatex = "R_n = \\sum_{i=1}^n f(a + i \\Delta x) \\Delta x \\quad \\text{where } \\Delta x = \\frac{b - a}{n}",
            formulaExplanation = "The Σ (sigma) means 'add them all up'. Δx is the common base width, and f(...) is the varying height of the i-th rectangle.",
            diagramType = DiagramType.INTERACTIVE_RIEMANN_SUM,
            diagramCaption = "Interactive Riemann Explorer: Drag the slider from n = 2 to n = 40 and watch the area converge!",
            exampleTitle = "Calculating a Left Riemann Sum with 4 Subintervals",
            exampleProblem = "Approximate the area under f(x) = x + 2 on [0, 4] using n = 4 left-endpoint rectangles (L₄).",
            exampleSteps = listOf(
                WorkedStep(1, "Find slice width Δx", "Δx = (4 - 0) / 4 = 1"),
                WorkedStep(2, "List left endpoints", "x = 0, 1, 2, 3"),
                WorkedStep(3, "Calculate heights for each left endpoint", "f(0) = 2, f(1) = 3, f(2) = 4, f(3) = 5"),
                WorkedStep(4, "Multiply each height by width Δx = 1 and sum", "L₄ = 1·(2 + 3 + 4 + 5) = 14")
            ),
            practiceProblem = PracticeProblem(
                prompt = "For f(x) = x on [0, 4] with n = 4, find the Right Riemann Sum R₄.",
                hint = "Right endpoints are 1, 2, 3, 4. Heights are 1, 2, 3, 4. Sum = 1(1 + 2 + 3 + 4).",
                solutionSteps = listOf(
                    "Δx = (4 - 0) / 4 = 1",
                    "Right endpoints: 1, 2, 3, 4",
                    "Heights: f(1)=1, f(2)=2, f(3)=3, f(4)=4",
                    "Sum = 1 * (1 + 2 + 3 + 4) = 10"
                ),
                finalAnswer = "10"
            ),
            commonMistake = "Using the rightmost endpoint in a Left Riemann Sum, or the leftmost endpoint in a Right Riemann Sum!",
            teacherTip = "Notice how Left Sum underestimated and Right Sum overestimated for an increasing function. The true area is right between them!"
        ),

        // Topic 18
        Topic(
            id = 18,
            partId = 4,
            partTitle = "Part 4 — Integration",
            chapterNumber = 18,
            title = "The Definite Integral as a Limit of Sums",
            shortTitle = "18. Definite Integrals",
            plainEnglishIntuition = "What happens if we let the number of rectangles shoot off to infinity (n -> ∞)? The width Δx shrinks down to an infinitesimal whisper called dx. The jagged sigma Σ stretches into the elegant elongated 'S' symbol: ∫ (from Latin 'Summa'). The approximation stops being an estimate and turns into the EXACT, pure, continuous area under the curve.",
            formulaTitle = "The Definite Integral Definition",
            formulaLatex = "\\int_a^b f(x)\\,dx = \\lim_{n \\to \\infty} \\sum_{i=1}^n f(x_i) \\Delta x",
            formulaExplanation = "a is the lower limit of integration. b is the upper limit. f(x) is the integrand (height). dx represents infinitesimal width.",
            diagramType = DiagramType.DEFINITE_INTEGRAL_SUM,
            diagramCaption = "From discrete rectangles to continuous ink: The discrete sum Σ transforms into the continuous integral ∫.",
            exampleTitle = "Geometric Evaluation of a Definite Integral",
            exampleProblem = "Evaluate ∫₀³ (2x) dx using basic geometry.",
            exampleSteps = listOf(
                WorkedStep(1, "Sketch the region under y = 2x from x = 0 to x = 3", "It forms a right triangle with base on the x-axis."),
                WorkedStep(2, "Determine base length", "Base = 3 - 0 = 3"),
                WorkedStep(3, "Determine height at x = 3", "Height = 2(3) = 6"),
                WorkedStep(4, "Apply triangle area formula 1/2 · base · height", "Area = 1/2 · (3) · (6) = 9"),
                WorkedStep(5, "Conclusion", "∫₀³ (2x) dx = 9")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Evaluate ∫₀⁴ 5 dx using geometry.",
                hint = "y = 5 is a flat horizontal line. The area under it is a rectangle of height 5 and base 4.",
                solutionSteps = listOf(
                    "Width = 4 - 0 = 4",
                    "Height = 5",
                    "Rectangle Area = Width * Height = 4 * 5 = 20"
                ),
                finalAnswer = "20"
            ),
            commonMistake = "Thinking area below the x-axis is positive! Definite integrals compute NET signed area: area above x-axis is (+), area below is (-).",
            teacherTip = "dx is not just punctuation! It tells you which variable you are integrating with respect to, and has physical units."
        ),

        // Topic 19
        Topic(
            id = 19,
            partId = 4,
            partTitle = "Part 4 — Integration",
            chapterNumber = 19,
            title = "The Fundamental Theorem of Calculus (FTC)",
            shortTitle = "19. Fundamental Theorem",
            plainEnglishIntuition = "This is the climax of all mathematics. Differentiation asks: 'What is the instantaneous slope?' Integration asks: 'What is the total accumulated area?' On the surface, slopes and areas appear to have nothing in common. The FTC reveals that they are INVERSE operations — like addition and subtraction! To calculate an area under f(x), you don't need infinite rectangles: you just find an antiderivative F(x) and subtract F(b) - F(a)!",
            formulaTitle = "The Fundamental Theorem of Calculus (Parts 1 & 2)",
            formulaLatex = "\\frac{d}{dx}\\left[\\int_a^x f(t)\\,dt\\right] = f(x) \\quad \\Big| \\quad \\int_a^b f(x)\\,dx = F(b) - F(a)",
            formulaExplanation = "F(x) is any function whose derivative equals f(x), meaning F'(x) = f(x). This turns hard area problems into simple subtraction!",
            diagramType = DiagramType.FTC_INVERSE_MACHINE,
            diagramCaption = "The Reversible Engine of Calculus: Differentiation and Integration are mirror-image inverse twins.",
            exampleTitle = "Evaluating an Integral using FTC Part 2",
            exampleProblem = "Evaluate ∫₁³ (3x^2) dx using the Fundamental Theorem.",
            exampleSteps = listOf(
                WorkedStep(1, "Find an antiderivative F(x) of 3x^2", "Ask: 'Whose derivative is 3x^2?' Answer: F(x) = x^3!"),
                WorkedStep(2, "Set up the evaluation bracket notation", "[x^3]₁³"),
                WorkedStep(3, "Evaluate at upper limit b = 3", "F(3) = 3^3 = 27"),
                WorkedStep(4, "Evaluate at lower limit a = 1", "F(1) = 1^3 = 1"),
                WorkedStep(5, "Subtract F(b) - F(a)", "27 - 1 = 26")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Evaluate ∫₀² (4x) dx using FTC.",
                hint = "Antiderivative of 4x is 2x^2. Compute 2(2)^2 - 2(0)^2.",
                solutionSteps = listOf(
                    "Antiderivative F(x) = 2x^2 (check: derivative of 2x^2 is 4x)",
                    "F(2) = 2(2^2) = 2(4) = 8",
                    "F(0) = 2(0^2) = 0",
                    "F(2) - F(0) = 8 - 0 = 8"
                ),
                finalAnswer = "8"
            ),
            commonMistake = "Subtracting backwards: doing F(a) - F(b) instead of Upper minus Lower F(b) - F(a)!",
            teacherTip = "Always double-check your antiderivative: differentiate your F(x) in your head. If it gives back f(x), you are safe!"
        ),

        // Topic 20
        Topic(
            id = 20,
            partId = 4,
            partTitle = "Part 4 — Integration",
            chapterNumber = 20,
            title = "Integration Rules & Techniques: Substitution",
            shortTitle = "20. Substitution & Rules",
            plainEnglishIntuition = "Just like differentiation had rules, integration has inverse rules. The Reverse Power Rule adds 1 to the exponent and divides by the new exponent: ∫ x^n dx = x^(n+1)/(n+1) + C. When functions are tangled up with their own derivatives, u-substitution acts as the reverse Chain Rule — like swapping currency into a cleaner denomination where the integral becomes effortless.",
            formulaTitle = "Reverse Power Rule & U-Substitution",
            formulaLatex = "\\int x^n\\,dx = \\frac{x^{n+1}}{n+1} + C \\quad \\Big| \\quad \\int f(u)\\,du",
            formulaExplanation = "Always remember + C (the arbitrary constant of integration) for indefinite integrals, because any constant differentiates to zero!",
            diagramType = DiagramType.SUBSTITUTION_PUZZLE,
            diagramCaption = "U-Substitution Puzzle: Spot the inner function u and its derivative buddy du hovering nearby!",
            exampleTitle = "Evaluating an Integral by U-Substitution",
            exampleProblem = "Find ∫ 2x · (x^2 + 5)^3 dx.",
            exampleSteps = listOf(
                WorkedStep(1, "Pick u to be the messy inside expression", "Let u = x^2 + 5"),
                WorkedStep(2, "Differentiate u to find du", "du = 2x dx"),
                WorkedStep(3, "Substitute u and du into the integral", "∫ (u)^3 du"),
                WorkedStep(4, "Integrate with the reverse power rule", "u^4 / 4 + C"),
                WorkedStep(5, "Substitute back the original x variable", "(x^2 + 5)^4 / 4 + C")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Find ∫ 3x^2 · e^(x^3) dx.",
                hint = "Let u = x^3, then du = 3x^2 dx. The integral becomes ∫ e^u du.",
                solutionSteps = listOf(
                    "u = x^3  =>  du = 3x^2 dx",
                    "Integral becomes ∫ e^u du",
                    "Integrate: e^u + C",
                    "Substitute back: e^(x^3) + C"
                ),
                finalAnswer = "e^(x^3) + C"
            ),
            commonMistake = "Forgetting '+ C' on indefinite integrals! Points are lost every year across the world for forgotten + C's.",
            teacherTip = "When looking for u, look for the term whose derivative is already sitting outside waiting to become du."
        ),

        // Topic 21
        Topic(
            id = 21,
            partId = 4,
            partTitle = "Part 4 — Integration",
            chapterNumber = 21,
            title = "Applications: Area Between Curves & Volumes",
            shortTitle = "21. Areas & Volumes",
            plainEnglishIntuition = "What if you need the area trapped between two competing curves, like profit = revenue - cost? Slice vertically: each slice has height (Top curve - Bottom curve) and width dx! We can even spin a 2D curve around the x-axis like a potter's wheel to create 3D solids of revolution (vases, cones, champagne glasses) where each slice is a thin circular coin of volume π·[r(x)]^2 dx.",
            formulaTitle = "Area Between Curves & Disk Volume",
            formulaLatex = "\\text{Area} = \\int_a^b [f(x) - g(x)]\\,dx \\quad \\Big| \\quad \\text{Volume} = \\int_a^b \\pi [f(x)]^2\\,dx",
            formulaExplanation = "For volumes: each slice is a cylindrical disc of radius r = f(x), area πr², and thickness dx.",
            diagramType = DiagramType.AREA_BETWEEN_CURVES,
            diagramCaption = "Top minus Bottom: Strip height is [f(x) - g(x)]. Summing all strips gives the trapped area!",
            exampleTitle = "Finding Area Trapped Between y = x and y = x^2",
            exampleProblem = "Find the area bounded between y = x (top) and y = x^2 (bottom).",
            exampleSteps = listOf(
                WorkedStep(1, "Find intersection points: set x = x^2", "x^2 - x = 0  =>  x(x - 1) = 0  =>  x = 0 and x = 1"),
                WorkedStep(2, "Identify which curve is on top between 0 and 1", "Test x = 0.5: x = 0.5 is greater than x^2 = 0.25. So Top = x, Bottom = x^2."),
                WorkedStep(3, "Set up the integral", "∫₀¹ (x - x^2) dx"),
                WorkedStep(4, "Antidifferentiate each term", "[x^2 / 2 - x^3 / 3]₀¹"),
                WorkedStep(5, "Evaluate upper minus lower", "(1/2 - 1/3) - (0 - 0) = 3/6 - 2/6 = 1/6")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Find the volume of the solid formed by rotating y = √x on [0, 4] around the x-axis.",
                hint = "Volume = ∫₀⁴ π·(√x)^2 dx = π·∫₀⁴ x dx.",
                solutionSteps = listOf(
                    "Radius r(x) = √x  =>  r^2 = x",
                    "V = π ∫₀⁴ x dx",
                    "Antiderivative = [x^2 / 2]₀⁴",
                    "= π * (16 / 2 - 0) = 8π"
                ),
                finalAnswer = "8π cubic units"
            ),
            commonMistake = "Flipping Top and Bottom, resulting in a negative area. Area is always positive!",
            teacherTip = "Always sketch the two curves first to clearly see which function sits on top over the interval."
        ),

        // Topic 22
        Topic(
            id = 22,
            partId = 5,
            partTitle = "Part 5 — Advanced Topics",
            chapterNumber = 22,
            title = "Sequences & Series: Adding to Infinity",
            shortTitle = "22. Sequences & Series",
            plainEnglishIntuition = "Can you add an infinite list of positive numbers together and end up with a finite, tidy sum? Zeno thought walking across a room was impossible because you must walk 1/2, then 1/4, then 1/8... but 1/2 + 1/4 + 1/8 + 1/16 + ... adds up to exactly 1! When the terms shrink fast enough, an infinite series 'converges'. If they don't, it explodes to infinity ('diverges').",
            formulaTitle = "Sum of an Infinite Geometric Series",
            formulaLatex = "S = \\frac{a}{1 - r} \\quad (|r| < 1)",
            formulaExplanation = "a is the first term. r is the common ratio multiplied at each step. If |r| ≥ 1, the series diverges.",
            diagramType = DiagramType.SERIES_DOMINOES,
            diagramCaption = "Infinite cake slicing: 1/2 + 1/4 + 1/8 + 1/16... fills up exactly 1 whole square!",
            exampleTitle = "Summing a Repeating Infinite Geometric Series",
            exampleProblem = "Find the exact sum of the series 6 + 3 + 1.5 + 0.75 + ... to infinity.",
            exampleSteps = listOf(
                WorkedStep(1, "Identify first term a", "a = 6"),
                WorkedStep(2, "Find common ratio r", "r = 3 / 6 = 1/2"),
                WorkedStep(3, "Check convergence condition", "|1/2| < 1, so it converges!"),
                WorkedStep(4, "Apply formula S = a / (1 - r)", "S = 6 / (1 - 1/2) = 6 / (0.5) = 12")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Find the sum of 10 + 2 + 0.4 + 0.08 + ...",
                hint = "a = 10, r = 2/10 = 0.2. Compute 10 / (1 - 0.2).",
                solutionSteps = listOf(
                    "a = 10, r = 0.2",
                    "S = 10 / (1 - 0.2) = 10 / 0.8",
                    "10 / (4/5) = 10 * 5/4 = 50 / 4 = 12.5"
                ),
                finalAnswer = "12.5"
            ),
            commonMistake = "Assuming that because individual terms shrink to zero, the sum must converge. (The Harmonic series 1 + 1/2 + 1/3 + 1/4 + ... shrinks to zero, yet diverges to infinity!)",
            teacherTip = "A sequence is a list of comma-separated numbers; a series is when you add them all up with plus signs."
        ),

        // Topic 23
        Topic(
            id = 23,
            partId = 5,
            partTitle = "Part 5 — Advanced Topics",
            chapterNumber = 23,
            title = "Taylor & Maclaurin Series: Approximating Any Function",
            shortTitle = "23. Taylor Series",
            plainEnglishIntuition = "Calculators cannot directly compute sin(1.3) or e^2.7. Inside their silicon chips, they use polynomials! Polynomials are friendly because they only require basic addition and multiplication. A Taylor Series lets us approximate ANY curved, wavy function using a custom-tailored polynomial whose derivatives match the original function's derivatives order-by-order at a central anchor point.",
            formulaTitle = "Taylor Series Centered at x = a",
            formulaLatex = "f(x) = \\sum_{n=0}^{\\infty} \\frac{f^{(n)}(a)}{n!} (x - a)^n",
            formulaExplanation = "fⁿ(a) is the n-th derivative evaluated at anchor a. n! (n factorial) handles repeated power-rule integrations.",
            diagramType = DiagramType.TAYLOR_SERIES_FIT,
            diagramCaption = "Polynomial hug: Degree 1 gives a tangent line; degree 3 bends with the curve; degree 7 hugs it tightly!",
            exampleTitle = "Building the Maclaurin Series for e^x",
            exampleProblem = "Find the first 4 terms of the Maclaurin series (centered at a = 0) for f(x) = e^x.",
            exampleSteps = listOf(
                WorkedStep(1, "Compute derivatives of e^x at x = 0", "f(0) = 1, f'(0) = 1, f''(0) = 1, f'''(0) = 1 (all derivatives are e^0 = 1!)"),
                WorkedStep(2, "Term 0 (n = 0)", "1 / 0! · x^0 = 1"),
                WorkedStep(3, "Term 1 (n = 1)", "1 / 1! · x^1 = x"),
                WorkedStep(4, "Term 2 (n = 2)", "1 / 2! · x^2 = x^2 / 2"),
                WorkedStep(5, "Term 3 (n = 3)", "1 / 3! · x^3 = x^3 / 6"),
                WorkedStep(6, "Combine series", "e^x ≈ 1 + x + x^2/2 + x^3/6 + ...")
            ),
            practiceProblem = PracticeProblem(
                prompt = "Using e^x ≈ 1 + x + x^2/2, approximate e^(0.1).",
                hint = "Substitute x = 0.1: 1 + 0.1 + (0.01)/2.",
                solutionSteps = listOf(
                    "1 + 0.1 = 1.1",
                    "(0.1)^2 / 2 = 0.01 / 2 = 0.005",
                    "1.1 + 0.005 = 1.105"
                ),
                finalAnswer = "1.105 (actual value is 1.10517... amazingly accurate!)"
            ),
            commonMistake = "Forgetting the factorial in the denominator: dividing by n instead of n!.",
            teacherTip = "Euler used this formula to discover e^(iπ) + 1 = 0, connecting e, i, π, 1, and 0 in one breathtaking equation."
        ),

        // Topic 24
        Topic(
            id = 24,
            partId = 5,
            partTitle = "Part 5 — Advanced Topics",
            chapterNumber = 24,
            title = "Multivariable Intro: Slicing with Partial Derivatives",
            shortTitle = "24. Partial Derivatives",
            plainEnglishIntuition = "Real life rarely has only one variable. A hiking trail's altitude z = f(x, y) depends on both your east-west coordinate x and north-south coordinate y. When you stand on a mountainside, the slope to your east might be a gentle descent, while the slope to your north is a sheer cliff! A 'partial derivative' measures your steepness in ONE chosen direction while freezing all other coordinates completely solid.",
            formulaTitle = "Partial Derivative Notation",
            formulaLatex = "\\frac{\\partial f}{\\partial x} = \\lim_{h \\to 0} \\frac{f(x + h, y) - f(x, y)}{h}",
            formulaExplanation = "The curly '∂' (del) warns you: when taking ∂f/∂x, treat y as if it were an ordinary constant number like 5 or 7!",
            diagramType = DiagramType.PARTIAL_DERIVATIVE_HILL,
            diagramCaption = "Slicing a 3D Mountain: Cut a vertical slice along the x-direction; the rim of the slice has slope ∂f/∂x.",
            exampleTitle = "Calculating Partial Derivatives of a 2-Variable Function",
            exampleProblem = "Given z = f(x, y) = 3x^2·y + 4y^3 - 5x, find ∂f/∂x and ∂f/∂y.",
            exampleSteps = listOf(
                WorkedStep(1, "To find ∂f/∂x: treat y as constant", "3(2x)·y + 0 - 5 = 6xy - 5"),
                WorkedStep(2, "To find ∂f/∂y: treat x as constant", "3x^2·(1) + 4(3y^2) - 0 = 3x^2 + 12y^2"),
                WorkedStep(3, "Result", "∂f/∂x = 6xy - 5    and    ∂f/∂y = 3x^2 + 12y^2")
            ),
            practiceProblem = PracticeProblem(
                prompt = "For f(x, y) = x^3 · y^2, find ∂f/∂x.",
                hint = "Keep y^2 as a constant multiplier, and differentiate x^3 to 3x^2.",
                solutionSteps = listOf(
                    "Treat y^2 as a constant factor",
                    "d/dx [x^3] = 3x^2",
                    "Multiply: 3x^2 · y^2"
                ),
                finalAnswer = "3x^2 · y^2"
            ),
            commonMistake = "Accidentally differentiating y when finding ∂f/∂x. Freeze y like an ice sculpture!",
            teacherTip = "Combining all partial derivatives into a vector creates the 'Gradient' ∇f, which points straight uphill in the steepest direction. This powers modern AI neural network training (Gradient Descent)!"
        ),

        // Topic 25
        Topic(
            id = 25,
            partId = 5,
            partTitle = "Part 5 — Advanced Topics",
            chapterNumber = 25,
            title = "Differential Equations: Why All Calculus Leads Here",
            shortTitle = "25. Differential Equations",
            plainEnglishIntuition = "In algebra, an equation asks you to find an unknown NUMBER (like x = 5). In differential equations, the equation asks you to find an unknown FUNCTION that relates to its own derivatives! Newton's laws of motion (F = m · d^2x/dt^2), the spread of epidemics, climate models, and radioactive decay are all differential equations. Calculus was created specifically so humanity could write and solve these cosmic laws.",
            formulaTitle = "The Natural Exponential Growth Equation",
            formulaLatex = "\\frac{dy}{dt} = k y \\implies y(t) = y_0 e^{kt}",
            formulaExplanation = "Says: 'The rate of growth is directly proportional to how much is currently present.' More bacteria => faster reproduction!",
            diagramType = DiagramType.DIFF_EQ_SLOPE_FIELD,
            diagramCaption = "Slope Field Currents: Tiny little tangent dashes show how solutions flow like leaves in a river.",
            exampleTitle = "Solving a Separable Differential Equation",
            exampleProblem = "Solve dy/dx = 2x·y for y as a function of x.",
            exampleSteps = listOf(
                WorkedStep(1, "Separate variables: move all y's to left, all x's to right", "(1/y) dy = 2x dx"),
                WorkedStep(2, "Integrate both sides", "∫ (1/y) dy = ∫ 2x dx"),
                WorkedStep(3, "Evaluate the integrals", "ln|y| = x^2 + C"),
                WorkedStep(4, "Exponentiate both sides with base e", "|y| = e^(x^2 + C) = e^C · e^(x^2)"),
                WorkedStep(5, "Let A = ±e^C (initial state constant)", "y(x) = A · e^(x^2)")
            ),
            practiceProblem = PracticeProblem(
                prompt = "If a bacterial colony grows according to dy/dt = 0.5y and starts with y(0) = 200, what is the population at t = 4 hours?",
                hint = "Use y(t) = y₀ · e^(kt) with y₀ = 200 and k = 0.5. At t = 4, e^(0.5 * 4) = e^2 ≈ 7.389.",
                solutionSteps = listOf(
                    "y(t) = 200 * e^(0.5t)",
                    "y(4) = 200 * e^(2)",
                    "e^2 ≈ 7.389",
                    "200 * 7.389 ≈ 1478"
                ),
                finalAnswer = "≈ 1,478 bacteria (exponential explosion!)"
            ),
            commonMistake = "Integrating without separating variables first: you cannot integrate dy if y is still hanging out on the dx side!",
            teacherTip = "Congratulations! You've journeyed from simple straight lines all the way to differential equations. You now possess the mathematical language of the universe."
        )
    )

    val quizzes: List<PartQuiz> = listOf(
        PartQuiz(
            partId = 1,
            partTitle = "Part 1 Quiz: Foundations",
            questions = listOf(
                QuizQuestion(
                    id = 101,
                    question = "What is the slope of the line passing through (2, 5) and (6, 17)?",
                    options = listOf("2", "3", "4", "12"),
                    correctIndex = 1,
                    explanation = "Slope m = (17 - 5) / (6 - 2) = 12 / 4 = 3."
                ),
                QuizQuestion(
                    id = 102,
                    question = "Why does the standard straight-line slope formula fail for curves like y = x²?",
                    options = listOf(
                        "Curves do not have coordinates",
                        "The steepness changes constantly from point to point",
                        "Curves can only have negative slopes",
                        "Straight-line slopes are always zero"
                    ),
                    correctIndex = 1,
                    explanation = "A curve's steepness is not constant; it varies continuously at every single point."
                ),
                QuizQuestion(
                    id = 103,
                    question = "If car position is s(t) = 5t² meters, what is the average speed between t = 1s and t = 2s?",
                    options = listOf("10 m/s", "15 m/s", "20 m/s", "25 m/s"),
                    correctIndex = 1,
                    explanation = "s(1) = 5, s(2) = 20. Average speed = (20 - 5) / (2 - 1) = 15 m/s."
                )
            )
        ),
        PartQuiz(
            partId = 2,
            partTitle = "Part 2 Quiz: Limits",
            questions = listOf(
                QuizQuestion(
                    id = 201,
                    question = "What is lim_{x -> 4} (x² - 16) / (x - 4)?",
                    options = listOf("0", "4", "8", "Undefined / DNE"),
                    correctIndex = 2,
                    explanation = "Factor (x-4)(x+4)/(x-4) = x+4. As x -> 4, 4 + 4 = 8."
                ),
                QuizQuestion(
                    id = 202,
                    question = "If lim_{x -> 2⁻} f(x) = 5 and lim_{x -> 2⁺} f(x) = 8, what is lim_{x -> 2} f(x)?",
                    options = listOf("6.5", "5", "8", "Does Not Exist (DNE)"),
                    correctIndex = 3,
                    explanation = "Left and right limits must agree for the overall limit to exist. Because 5 ≠ 8, it DNE."
                ),
                QuizQuestion(
                    id = 203,
                    question = "Which condition is NOT required for f(x) to be continuous at x = c?",
                    options = listOf(
                        "f(c) is defined",
                        "lim_{x -> c} f(x) exists",
                        "f'(c) must equal 0",
                        "lim_{x -> c} f(x) = f(c)"
                    ),
                    correctIndex = 2,
                    explanation = "A function does not need a flat slope (f'=0) to be continuous; it just needs no jumps or holes."
                )
            )
        ),
        PartQuiz(
            partId = 3,
            partTitle = "Part 3 Quiz: Differentiation",
            questions = listOf(
                QuizQuestion(
                    id = 301,
                    question = "What is the derivative of f(x) = 4x³ - 5x + 9?",
                    options = listOf("12x² - 5", "12x³ - 5", "4x² - 5", "12x² - 5x"),
                    correctIndex = 0,
                    explanation = "Power rule: 4(3x²) - 5(1) + 0 = 12x² - 5."
                ),
                QuizQuestion(
                    id = 302,
                    question = "Using the Chain Rule, what is the derivative of sin(3x)?",
                    options = listOf("cos(3x)", "3 cos(3x)", "-3 cos(3x)", "3 sin(3x)"),
                    correctIndex = 1,
                    explanation = "d/dx[sin(u)] = cos(u) · u'. Here u = 3x, so derivative is 3·cos(3x)."
                ),
                QuizQuestion(
                    id = 303,
                    question = "At a local maximum or minimum of a smooth curve, what is true about the tangent line?",
                    options = listOf(
                        "It is vertical",
                        "Its slope is 0 (horizontal)",
                        "It does not exist",
                        "Its slope is equal to 1"
                    ),
                    correctIndex = 1,
                    explanation = "At peaks and valleys, the tangent line flattens out, meaning f'(x) = 0."
                )
            )
        ),
        PartQuiz(
            partId = 4,
            partTitle = "Part 4 Quiz: Integration",
            questions = listOf(
                QuizQuestion(
                    id = 401,
                    question = "Evaluate the definite integral ∫₀³ (2x) dx:",
                    options = listOf("6", "9", "12", "18"),
                    correctIndex = 1,
                    explanation = "Antiderivative is x². [x²]₀³ = 3² - 0² = 9."
                ),
                QuizQuestion(
                    id = 402,
                    question = "What does the Fundamental Theorem of Calculus prove?",
                    options = listOf(
                        "Every equation has a solution",
                        "Differentiation and Integration are inverse operations",
                        "All curves are circles",
                        "Area under curves is always equal to 0"
                    ),
                    correctIndex = 1,
                    explanation = "FTC proves that taking the derivative of an accumulation area function restores the original rate function."
                ),
                QuizQuestion(
                    id = 403,
                    question = "Using u-substitution, what is ∫ 2x · e^(x²) dx?",
                    options = listOf("e^(x²) + C", "2e^(x²) + C", "x² · e^(x²) + C", "e^(2x) + C"),
                    correctIndex = 0,
                    explanation = "Let u = x², du = 2x dx. The integral becomes ∫ e^u du = e^u + C = e^(x²) + C."
                )
            )
        ),
        PartQuiz(
            partId = 5,
            partTitle = "Part 5 Quiz: Advanced Topics",
            questions = listOf(
                QuizQuestion(
                    id = 501,
                    question = "What is the sum of the infinite geometric series 8 + 4 + 2 + 1 + ...?",
                    options = listOf("14", "15", "16", "Infinity"),
                    correctIndex = 2,
                    explanation = "First term a = 8, ratio r = 0.5. Sum = a / (1 - r) = 8 / (1 - 0.5) = 16."
                ),
                QuizQuestion(
                    id = 502,
                    question = "For f(x, y) = 4x²y + 7y², what is the partial derivative ∂f/∂x?",
                    options = listOf("8xy", "4x² + 14y", "8x + 14y", "8xy + 14y"),
                    correctIndex = 0,
                    explanation = "Treat y as constant: d/dx[4x²y] = 8xy, and 7y² differentiates to 0."
                ),
                QuizQuestion(
                    id = 503,
                    question = "What type of equation relates an unknown function to its own rate of change?",
                    options = listOf("Linear algebra equation", "Differential equation", "Quadratic formula", "Pythagorean theorem"),
                    correctIndex = 1,
                    explanation = "A differential equation connects a function y(t) with its derivatives dy/dt."
                )
            )
        )
    )

    fun getTopic(id: Int): Topic {
        return topics.find { it.id == id } ?: topics.first()
    }

    fun getPart(id: Int): Part {
        return parts.find { it.id == id } ?: parts.first()
    }

    fun getQuiz(partId: Int): PartQuiz? {
        return quizzes.find { it.partId == partId }
    }
}
