package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ustermetrics.clarabel4j.Status.SOLVED;
import static java.lang.Math.exp;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    private static final double TOLERANCE = 1e-8;

    @Test
    void solveLinearProgramReturnsExpectedSolution() {
        // Linear program from the Clarabel examples
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_lp.c
        val p = new Matrix(2, 2, new long[]{0, 0, 0}, new long[]{}, new double[]{});
        val q = new double[]{1., -1.};
        val a = new Matrix(4, 2, new long[]{0, 2, 4}, new long[]{0, 2, 1, 3}, new double[]{1., -1., 1., -1.});
        val b = new double[]{1., 1., 1., 1.};
        final List<Cone> cones = List.of(new NonnegativeCone(4));

        try (val model = new Model()) {
            model.setup(p, q, a, b, cones);

            val parameters = Parameters.builder()
                    .equilibrateEnable(true)
                    .equilibrateMaxIter(50)
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{-1., 1.}, model.x(), TOLERANCE);
            assertArrayEquals(new double[]{0., 1., 1., 0.}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{2., 0., 0., 2.}, model.s(), TOLERANCE);
            assertEquals(-2., model.objVal(), TOLERANCE);
            assertEquals(-2., model.objValDual(), TOLERANCE);
            assertTrue(model.solveTime() > 0.);
            assertEquals(6, model.iterations());
            assertEquals(0., model.rPrim(), TOLERANCE);
            assertEquals(0., model.rDual(), TOLERANCE);
        }
    }

    @Test
    void solveLinearProgramWithoutMatrixPReturnsExpectedSolution() {
        // Linear program from the Clarabel examples using model.setup() without matrix P
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_lp.c
        val q = new double[]{1., -1.};
        val a = new Matrix(4, 2, new long[]{0, 2, 4}, new long[]{0, 2, 1, 3}, new double[]{1., -1., 1., -1.});
        val b = new double[]{1., 1., 1., 1.};
        final List<Cone> cones = List.of(new NonnegativeCone(4));

        try (val model = new Model()) {
            model.setup(q, a, b, cones);

            val parameters = Parameters.builder()
                    .equilibrateEnable(true)
                    .equilibrateMaxIter(50)
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{-1., 1.}, model.x(), TOLERANCE);
            assertArrayEquals(new double[]{0., 1., 1., 0.}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{2., 0., 0., 2.}, model.s(), TOLERANCE);
            assertEquals(-2., model.objVal(), TOLERANCE);
            assertEquals(-2., model.objValDual(), TOLERANCE);
            assertTrue(model.solveTime() > 0.);
            assertEquals(6, model.iterations());
            assertEquals(0., model.rPrim(), TOLERANCE);
            assertEquals(0., model.rDual(), TOLERANCE);
        }
    }

    @Test
    void solveQuadraticProgramReturnsExpectedSolution() {
        // Quadratic program from the Clarabel examples
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_qp.c
        val p = new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4.});
        val q = new double[]{-1., -4.};
        val a = new Matrix(5, 2, new long[]{0, 3, 6}, new long[]{0, 1, 3, 0, 2, 4},
                new double[]{1., 1., -1., -2., 1., -1.});
        val b = new double[]{0., 1., 1., 1., 1.};
        val cones = List.of(new ZeroCone(1), new NonnegativeCone(4));

        try (val model = new Model()) {
            model.setup(p, q, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{0.4285714282, 0.2142857141}, model.x(), TOLERANCE);
            assertArrayEquals(new double[]{-1.5714285714, 0., 0., 0., 0.}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{0., 0.5714285718, 0.7857142859, 1.4285714282, 1.2142857141}, model.s(),
                    TOLERANCE);
        }
    }

    @Test
    void solveSecondOrderConeProgramReturnsExpectedSolution() {
        // Second-order cone program from the Clarabel examples
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_socp.c
        val p = new Matrix(2, 2, new long[]{0, 0, 1}, new long[]{1}, new double[]{2.});
        val a = new Matrix(3, 2, new long[]{0, 1, 2}, new long[]{1, 2}, new double[]{-2., -1.});
        val b = new double[]{1., -2., -2.};
        final List<Cone> cones = List.of(new SecondOrderCone(3));

        try (val model = new Model()) {
            model.setup(p, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1., 1.}, model.x(), TOLERANCE);
            assertArrayEquals(new double[]{2., 0., 2.}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{1., 0., -1.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solveExponentialConeProgramReturnsExpectedSolution() {
        // Exponential cone program from the Clarabel examples
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_expcone.c
        val p = new Matrix(3, 3, new long[]{0, 0, 0, 0}, new long[]{}, new double[]{});
        val q = new double[]{-1., 0., 0.};
        val a = new Matrix(5, 3, new long[]{0, 1, 3, 5}, new long[]{0, 1, 3, 2, 4},
                new double[]{-1., -1., 1., -1., 1.});
        val b = new double[]{0., 0., 0., 1., exp(5.)};
        val cones = List.of(new ExponentialCone(), new ZeroCone(2));

        try (val model = new Model()) {
            model.setup(p, q, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{4.9999999409, 1., 148.4131589664}, model.x(), TOLERANCE);
            assertArrayEquals(new double[]{-1., 4.0001066439, 0.0067372289, 4.0001066434, 0.0067372278},
                    model.z(), TOLERANCE);
            assertArrayEquals(new double[]{4.9999999409, 1., 148.4131589675, 0., 0.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solveExponentialConeProgramWithoutMatrixPReturnsExpectedSolution() {
        // Exponential cone program from the Clarabel examples using model.setup() without matrix P
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_expcone.c
        val q = new double[]{-1., 0., 0.};
        val a = new Matrix(5, 3, new long[]{0, 1, 3, 5}, new long[]{0, 1, 3, 2, 4},
                new double[]{-1., -1., 1., -1., 1.});
        val b = new double[]{0., 0., 0., 1., exp(5.)};
        val cones = List.of(new ExponentialCone(), new ZeroCone(2));

        try (val model = new Model()) {
            model.setup(q, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{4.9999999409, 1., 148.4131589664}, model.x(), TOLERANCE);
            assertArrayEquals(new double[]{-1., 4.0001066439, 0.0067372289, 4.0001066434, 0.0067372278},
                    model.z(), TOLERANCE);
            assertArrayEquals(new double[]{4.9999999409, 1., 148.4131589675, 0., 0.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solvePowerConeProgramReturnsExpectedSolution() {
        // Power cone program from the Clarabel examples
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_powcone.c
        val p = new Matrix(6, 6, new long[]{0, 0, 0, 0, 0, 0, 0}, new long[]{}, new double[]{});
        val q = new double[]{0., 0., -1., 0., 0., -1.};
        val a = new Matrix(8, 6, new long[]{0, 2, 4, 5, 7, 9, 10}, new long[]{0, 6, 1, 6, 2, 3, 6, 4, 7, 5},
                new double[]{-1., 1., -1., 2., -1., -1., 3., -1., 1., -1.});
        val b = new double[]{0., 0., 0., 0., 0., 0., 3., 1.};
        val cones = List.of(new PowerCone(0.6), new PowerCone(0.1), new ZeroCone(1), new ZeroCone(1));

        try (val model = new Model()) {
            model.setup(p, q, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .maxIter(100)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1.6818793424, 0.5605840338, 1.0837601801, 0.0656508579, 1., 0.7615949175},
                    model.x(), TOLERANCE);
            assertArrayEquals(new double[]{0.3866364120, 0.7732728161, -1., 1.1599092208, 0.6854458909, -1.,
                    0.3866364045, 0.6854458815}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{1.6818793488, 0.5605840398, 1.0837601801, 0.0656508625, 1., 0.7615949175,
                    0., 0.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solvePowerConeProgramWithoutMatrixPReturnsExpectedSolution() {
        // Power cone program from the Clarabel examples using model.setup() without matrix P
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_powcone.c
        val q = new double[]{0., 0., -1., 0., 0., -1.};
        val a = new Matrix(8, 6, new long[]{0, 2, 4, 5, 7, 9, 10}, new long[]{0, 6, 1, 6, 2, 3, 6, 4, 7, 5},
                new double[]{-1., 1., -1., 2., -1., -1., 3., -1., 1., -1.});
        val b = new double[]{0., 0., 0., 0., 0., 0., 3., 1.};
        val cones = List.of(new PowerCone(0.6), new PowerCone(0.1), new ZeroCone(1), new ZeroCone(1));

        try (val model = new Model()) {
            model.setup(q, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .maxIter(100)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1.6818793424, 0.5605840338, 1.0837601801, 0.0656508579, 1., 0.7615949175},
                    model.x(), TOLERANCE);
            assertArrayEquals(new double[]{0.3866364120, 0.7732728161, -1., 1.1599092208, 0.6854458909, -1.,
                    0.3866364045, 0.6854458815}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{1.6818793488, 0.5605840398, 1.0837601801, 0.0656508625, 1., 0.7615949175,
                    0., 0.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solveGeneralizedPowerConeProgramReturnsExpectedSolution() {
        // Generalized power cone program from the Clarabel examples
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_genpowcone.c
        val p = new Matrix(6, 6, new long[]{0, 0, 0, 0, 0, 0, 0}, new long[]{}, new double[]{});
        val q = new double[]{0., 0., -1., 0., 0., -1.};
        val a = new Matrix(8, 6, new long[]{0, 2, 4, 5, 7, 9, 10}, new long[]{0, 6, 1, 6, 2, 3, 6, 4, 7, 5},
                new double[]{-1., 1., -1., 2., -1., -1., 3., -1., 1., -1.});
        val b = new double[]{0., 0., 0., 0., 0., 0., 3., 1.};
        val cones = List.of(new GenPowerCone(new double[]{0.6, 0.4}, 1), new GenPowerCone(new double[]{0.1, 0.9}, 1),
                new ZeroCone(1), new ZeroCone(1));

        try (val model = new Model()) {
            model.setup(p, q, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .maxIter(100)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1.6818105475, 0.5606033992, 1.0837485448, 0.0656608752, 1., 0.7616065300},
                    model.x(), TOLERANCE);
            assertArrayEquals(new double[]{0.3866364127, 0.7732728115, -1., 1.1599092115, 0.6854458898, -1.,
                    0.3866363997, 0.6854458735}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{1.6818105584, 0.5606034095, 1.0837485446, 0.0656608832, 1., 0.7616065300,
                    0., 0.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solveGeneralizedPowerConeProgramWithoutMatrixPReturnsExpectedSolution() {
        // Generalized power cone program from the Clarabel examples using model.setup() without matrix P
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_genpowcone.c
        val q = new double[]{0., 0., -1., 0., 0., -1.};
        val a = new Matrix(8, 6, new long[]{0, 2, 4, 5, 7, 9, 10}, new long[]{0, 6, 1, 6, 2, 3, 6, 4, 7, 5},
                new double[]{-1., 1., -1., 2., -1., -1., 3., -1., 1., -1.});
        val b = new double[]{0., 0., 0., 0., 0., 0., 3., 1.};
        val cones = List.of(new GenPowerCone(new double[]{0.6, 0.4}, 1), new GenPowerCone(new double[]{0.1, 0.9}, 1),
                new ZeroCone(1), new ZeroCone(1));

        try (val model = new Model()) {
            model.setup(q, a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .maxIter(100)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1.6818105475, 0.5606033992, 1.0837485448, 0.0656608752, 1., 0.7616065300},
                    model.x(), TOLERANCE);
            assertArrayEquals(new double[]{0.3866364127, 0.7732728115, -1., 1.1599092115, 0.6854458898, -1.,
                    0.3866363997, 0.6854458735}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{1.6818105584, 0.5606034095, 1.0837485446, 0.0656608832, 1., 0.7616065300,
                    0., 0.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solveFeasibilityProblemReturnsExpectedSolution() {
        // [[6., 0.],
        //  [0., 4.]]
        val a = new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4.});
        val b = new double[]{6., 1.};
        final List<Cone> cones = List.of(new ZeroCone(2));

        try (val model = new Model()) {
            model.setup(a, b, cones);

            val parameters = Parameters.builder()
                    .verbose(true)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1., 0.25}, model.x(), TOLERANCE);
            assertArrayEquals(new double[]{0., 0.}, model.z(), TOLERANCE);
            assertArrayEquals(new double[]{0., 0.}, model.s(), TOLERANCE);
        }
    }

    @Test
    void solveUnconstrainedQuadraticProgramReturnsExpectedSolution() {
        // [[6., 0.],
        //  [0., 4.]]
        val p = new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4.});
        val q = new double[]{-1., -4.};

        try (val model = new Model()) {
            model.setup(p, q);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1. / 6., 1.}, model.x(), TOLERANCE);
            assertEquals(0, model.z().length);
            assertEquals(0, model.s().length);
        }
    }

    @Test
    void solveUnconstrainedPureQuadraticProgramReturnsExpectedSolution() {
        // [[6., 0.],
        //  [0., 4.]]
        val p = new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4.});

        try (val model = new Model()) {
            model.setup(p);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{0., 0.}, model.x(), TOLERANCE);
            assertEquals(0, model.z().length);
            assertEquals(0, model.s().length);
        }
    }

    @Test
    void solveProblemTwiceWithCleanupInBetweenReturnsExpectedSolution() {
        // [[6., 0.],
        //  [0., 4.]]
        val p = new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4.});
        var q = new double[]{-1., -4.};

        try (val model = new Model()) {
            model.setup(p, q);

            val parameters = Parameters.builder()
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            var status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1. / 6., 1.}, model.x(), TOLERANCE);
            assertEquals(0, model.z().length);
            assertEquals(0, model.s().length);

            model.cleanup();

            q = new double[]{-2., -4.};

            model.setup(p, q);

            status = model.optimize();

            assertEquals(SOLVED, status);
            assertArrayEquals(new double[]{1. / 3., 1.}, model.x(), TOLERANCE);
            assertEquals(0, model.z().length);
            assertEquals(0, model.s().length);
        }
    }

    @Test
    void setupAfterOptimizeThrowsException() {
        val p = new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4.});

        val exception = assertThrows(IllegalStateException.class, () -> {
            try (val model = new Model()) {
                model.setup(p);
                model.setParameters(Parameters.builder().verbose(false).build());
                model.optimize();
                model.setup(p);
            }
        });

        assertEquals("model must be in stage new", exception.getMessage());
    }

    @Test
    void setParametersBeforeSetupThrowsException() {
        val exception = assertThrows(IllegalStateException.class, () -> {
            try (val model = new Model()) {
                model.setParameters(Parameters.builder().verbose(false).build());
            }
        });

        assertEquals("model must not be in stage new", exception.getMessage());
    }

    @Test
    void optimizeBeforeSetupThrowsException() {
        val exception = assertThrows(IllegalStateException.class, () -> {
            try (val model = new Model()) {
                model.optimize();
            }
        });

        assertEquals("model must not be in stage new", exception.getMessage());
    }

    @Test
    void getSolutionBeforeOptimizeThrowsException() {
        val exception = assertThrows(IllegalStateException.class, () -> {
            try (val model = new Model()) {
                model.x();
            }
        });

        assertEquals("model must be in stage optimized", exception.getMessage());
    }

}
