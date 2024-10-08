// Generated by jextract

package com.ustermetrics.clarabel4j.bindings;

import java.lang.invoke.*;
import java.lang.foreign.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * struct ClarabelDefaultInfo_f64 {
 *     double mu;
 *     double sigma;
 *     double step_length;
 *     uint32_t iterations;
 *     double cost_primal;
 *     double cost_dual;
 *     double res_primal;
 *     double res_dual;
 *     double res_primal_inf;
 *     double res_dual_inf;
 *     double gap_abs;
 *     double gap_rel;
 *     double ktratio;
 *     double solve_time;
 *     enum ClarabelSolverStatus status;
 * }
 * }
 */
public class ClarabelDefaultInfo_f64 {

    ClarabelDefaultInfo_f64() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        Clarabel_h.C_DOUBLE.withName("mu"),
        Clarabel_h.C_DOUBLE.withName("sigma"),
        Clarabel_h.C_DOUBLE.withName("step_length"),
        Clarabel_h.C_INT.withName("iterations"),
        MemoryLayout.paddingLayout(4),
        Clarabel_h.C_DOUBLE.withName("cost_primal"),
        Clarabel_h.C_DOUBLE.withName("cost_dual"),
        Clarabel_h.C_DOUBLE.withName("res_primal"),
        Clarabel_h.C_DOUBLE.withName("res_dual"),
        Clarabel_h.C_DOUBLE.withName("res_primal_inf"),
        Clarabel_h.C_DOUBLE.withName("res_dual_inf"),
        Clarabel_h.C_DOUBLE.withName("gap_abs"),
        Clarabel_h.C_DOUBLE.withName("gap_rel"),
        Clarabel_h.C_DOUBLE.withName("ktratio"),
        Clarabel_h.C_DOUBLE.withName("solve_time"),
        Clarabel_h.C_INT.withName("status"),
        MemoryLayout.paddingLayout(4)
    ).withName("ClarabelDefaultInfo_f64");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfDouble mu$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("mu"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double mu
     * }
     */
    public static final OfDouble mu$layout() {
        return mu$LAYOUT;
    }

    private static final long mu$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double mu
     * }
     */
    public static final long mu$offset() {
        return mu$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double mu
     * }
     */
    public static double mu(MemorySegment struct) {
        return struct.get(mu$LAYOUT, mu$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double mu
     * }
     */
    public static void mu(MemorySegment struct, double fieldValue) {
        struct.set(mu$LAYOUT, mu$OFFSET, fieldValue);
    }

    private static final OfDouble sigma$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("sigma"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double sigma
     * }
     */
    public static final OfDouble sigma$layout() {
        return sigma$LAYOUT;
    }

    private static final long sigma$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double sigma
     * }
     */
    public static final long sigma$offset() {
        return sigma$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double sigma
     * }
     */
    public static double sigma(MemorySegment struct) {
        return struct.get(sigma$LAYOUT, sigma$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double sigma
     * }
     */
    public static void sigma(MemorySegment struct, double fieldValue) {
        struct.set(sigma$LAYOUT, sigma$OFFSET, fieldValue);
    }

    private static final OfDouble step_length$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("step_length"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double step_length
     * }
     */
    public static final OfDouble step_length$layout() {
        return step_length$LAYOUT;
    }

    private static final long step_length$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double step_length
     * }
     */
    public static final long step_length$offset() {
        return step_length$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double step_length
     * }
     */
    public static double step_length(MemorySegment struct) {
        return struct.get(step_length$LAYOUT, step_length$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double step_length
     * }
     */
    public static void step_length(MemorySegment struct, double fieldValue) {
        struct.set(step_length$LAYOUT, step_length$OFFSET, fieldValue);
    }

    private static final OfInt iterations$LAYOUT = (OfInt)$LAYOUT.select(groupElement("iterations"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint32_t iterations
     * }
     */
    public static final OfInt iterations$layout() {
        return iterations$LAYOUT;
    }

    private static final long iterations$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint32_t iterations
     * }
     */
    public static final long iterations$offset() {
        return iterations$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint32_t iterations
     * }
     */
    public static int iterations(MemorySegment struct) {
        return struct.get(iterations$LAYOUT, iterations$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint32_t iterations
     * }
     */
    public static void iterations(MemorySegment struct, int fieldValue) {
        struct.set(iterations$LAYOUT, iterations$OFFSET, fieldValue);
    }

    private static final OfDouble cost_primal$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("cost_primal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double cost_primal
     * }
     */
    public static final OfDouble cost_primal$layout() {
        return cost_primal$LAYOUT;
    }

    private static final long cost_primal$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double cost_primal
     * }
     */
    public static final long cost_primal$offset() {
        return cost_primal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double cost_primal
     * }
     */
    public static double cost_primal(MemorySegment struct) {
        return struct.get(cost_primal$LAYOUT, cost_primal$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double cost_primal
     * }
     */
    public static void cost_primal(MemorySegment struct, double fieldValue) {
        struct.set(cost_primal$LAYOUT, cost_primal$OFFSET, fieldValue);
    }

    private static final OfDouble cost_dual$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("cost_dual"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double cost_dual
     * }
     */
    public static final OfDouble cost_dual$layout() {
        return cost_dual$LAYOUT;
    }

    private static final long cost_dual$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double cost_dual
     * }
     */
    public static final long cost_dual$offset() {
        return cost_dual$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double cost_dual
     * }
     */
    public static double cost_dual(MemorySegment struct) {
        return struct.get(cost_dual$LAYOUT, cost_dual$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double cost_dual
     * }
     */
    public static void cost_dual(MemorySegment struct, double fieldValue) {
        struct.set(cost_dual$LAYOUT, cost_dual$OFFSET, fieldValue);
    }

    private static final OfDouble res_primal$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("res_primal"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double res_primal
     * }
     */
    public static final OfDouble res_primal$layout() {
        return res_primal$LAYOUT;
    }

    private static final long res_primal$OFFSET = 48;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double res_primal
     * }
     */
    public static final long res_primal$offset() {
        return res_primal$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double res_primal
     * }
     */
    public static double res_primal(MemorySegment struct) {
        return struct.get(res_primal$LAYOUT, res_primal$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double res_primal
     * }
     */
    public static void res_primal(MemorySegment struct, double fieldValue) {
        struct.set(res_primal$LAYOUT, res_primal$OFFSET, fieldValue);
    }

    private static final OfDouble res_dual$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("res_dual"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double res_dual
     * }
     */
    public static final OfDouble res_dual$layout() {
        return res_dual$LAYOUT;
    }

    private static final long res_dual$OFFSET = 56;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double res_dual
     * }
     */
    public static final long res_dual$offset() {
        return res_dual$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double res_dual
     * }
     */
    public static double res_dual(MemorySegment struct) {
        return struct.get(res_dual$LAYOUT, res_dual$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double res_dual
     * }
     */
    public static void res_dual(MemorySegment struct, double fieldValue) {
        struct.set(res_dual$LAYOUT, res_dual$OFFSET, fieldValue);
    }

    private static final OfDouble res_primal_inf$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("res_primal_inf"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double res_primal_inf
     * }
     */
    public static final OfDouble res_primal_inf$layout() {
        return res_primal_inf$LAYOUT;
    }

    private static final long res_primal_inf$OFFSET = 64;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double res_primal_inf
     * }
     */
    public static final long res_primal_inf$offset() {
        return res_primal_inf$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double res_primal_inf
     * }
     */
    public static double res_primal_inf(MemorySegment struct) {
        return struct.get(res_primal_inf$LAYOUT, res_primal_inf$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double res_primal_inf
     * }
     */
    public static void res_primal_inf(MemorySegment struct, double fieldValue) {
        struct.set(res_primal_inf$LAYOUT, res_primal_inf$OFFSET, fieldValue);
    }

    private static final OfDouble res_dual_inf$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("res_dual_inf"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double res_dual_inf
     * }
     */
    public static final OfDouble res_dual_inf$layout() {
        return res_dual_inf$LAYOUT;
    }

    private static final long res_dual_inf$OFFSET = 72;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double res_dual_inf
     * }
     */
    public static final long res_dual_inf$offset() {
        return res_dual_inf$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double res_dual_inf
     * }
     */
    public static double res_dual_inf(MemorySegment struct) {
        return struct.get(res_dual_inf$LAYOUT, res_dual_inf$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double res_dual_inf
     * }
     */
    public static void res_dual_inf(MemorySegment struct, double fieldValue) {
        struct.set(res_dual_inf$LAYOUT, res_dual_inf$OFFSET, fieldValue);
    }

    private static final OfDouble gap_abs$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("gap_abs"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double gap_abs
     * }
     */
    public static final OfDouble gap_abs$layout() {
        return gap_abs$LAYOUT;
    }

    private static final long gap_abs$OFFSET = 80;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double gap_abs
     * }
     */
    public static final long gap_abs$offset() {
        return gap_abs$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double gap_abs
     * }
     */
    public static double gap_abs(MemorySegment struct) {
        return struct.get(gap_abs$LAYOUT, gap_abs$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double gap_abs
     * }
     */
    public static void gap_abs(MemorySegment struct, double fieldValue) {
        struct.set(gap_abs$LAYOUT, gap_abs$OFFSET, fieldValue);
    }

    private static final OfDouble gap_rel$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("gap_rel"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double gap_rel
     * }
     */
    public static final OfDouble gap_rel$layout() {
        return gap_rel$LAYOUT;
    }

    private static final long gap_rel$OFFSET = 88;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double gap_rel
     * }
     */
    public static final long gap_rel$offset() {
        return gap_rel$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double gap_rel
     * }
     */
    public static double gap_rel(MemorySegment struct) {
        return struct.get(gap_rel$LAYOUT, gap_rel$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double gap_rel
     * }
     */
    public static void gap_rel(MemorySegment struct, double fieldValue) {
        struct.set(gap_rel$LAYOUT, gap_rel$OFFSET, fieldValue);
    }

    private static final OfDouble ktratio$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("ktratio"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double ktratio
     * }
     */
    public static final OfDouble ktratio$layout() {
        return ktratio$LAYOUT;
    }

    private static final long ktratio$OFFSET = 96;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double ktratio
     * }
     */
    public static final long ktratio$offset() {
        return ktratio$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double ktratio
     * }
     */
    public static double ktratio(MemorySegment struct) {
        return struct.get(ktratio$LAYOUT, ktratio$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double ktratio
     * }
     */
    public static void ktratio(MemorySegment struct, double fieldValue) {
        struct.set(ktratio$LAYOUT, ktratio$OFFSET, fieldValue);
    }

    private static final OfDouble solve_time$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("solve_time"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double solve_time
     * }
     */
    public static final OfDouble solve_time$layout() {
        return solve_time$LAYOUT;
    }

    private static final long solve_time$OFFSET = 104;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double solve_time
     * }
     */
    public static final long solve_time$offset() {
        return solve_time$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double solve_time
     * }
     */
    public static double solve_time(MemorySegment struct) {
        return struct.get(solve_time$LAYOUT, solve_time$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double solve_time
     * }
     */
    public static void solve_time(MemorySegment struct, double fieldValue) {
        struct.set(solve_time$LAYOUT, solve_time$OFFSET, fieldValue);
    }

    private static final OfInt status$LAYOUT = (OfInt)$LAYOUT.select(groupElement("status"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * enum ClarabelSolverStatus status
     * }
     */
    public static final OfInt status$layout() {
        return status$LAYOUT;
    }

    private static final long status$OFFSET = 112;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * enum ClarabelSolverStatus status
     * }
     */
    public static final long status$offset() {
        return status$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * enum ClarabelSolverStatus status
     * }
     */
    public static int status(MemorySegment struct) {
        return struct.get(status$LAYOUT, status$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * enum ClarabelSolverStatus status
     * }
     */
    public static void status(MemorySegment struct, int fieldValue) {
        struct.set(status$LAYOUT, status$OFFSET, fieldValue);
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this struct
     */
    public static long sizeof() { return layout().byteSize(); }

    /**
     * Allocate a segment of size {@code layout().byteSize()} using {@code allocator}
     */
    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }

    /**
     * Allocate an array of size {@code elementCount} using {@code allocator}.
     * The returned segment has size {@code elementCount * layout().byteSize()}.
     */
    public static MemorySegment allocateArray(long elementCount, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

