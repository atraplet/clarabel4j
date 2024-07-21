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
 * struct ClarabelSupportedConeT_f64 {
 *     ClarabelSupportedConeT_Tag tag;
 *     union {
 *         struct {
 *             uintptr_t zero_cone_t;
 *         };
 *         struct {
 *             uintptr_t nonnegative_cone_t;
 *         };
 *         struct {
 *             uintptr_t second_order_cone_t;
 *         };
 *         struct {
 *             double power_cone_t;
 *         };
 *         struct {
 *             double *genpow_cone_alpha_t;
 *             uintptr_t genpow_cone_dim1_t;
 *             uintptr_t genpow_cone_dim2_t;
 *         };
 *     };
 * }
 * }
 */
public class ClarabelSupportedConeT_f64 {

    ClarabelSupportedConeT_f64() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        Clarabel_h.C_INT.withName("tag"),
        MemoryLayout.paddingLayout(4),
        MemoryLayout.unionLayout(
            MemoryLayout.structLayout(
                Clarabel_h.C_LONG_LONG.withName("zero_cone_t")
            ).withName("$anon$66:9"),
            MemoryLayout.structLayout(
                Clarabel_h.C_LONG_LONG.withName("nonnegative_cone_t")
            ).withName("$anon$70:9"),
            MemoryLayout.structLayout(
                Clarabel_h.C_LONG_LONG.withName("second_order_cone_t")
            ).withName("$anon$74:9"),
            MemoryLayout.structLayout(
                Clarabel_h.C_DOUBLE.withName("power_cone_t")
            ).withName("$anon$79:9"),
            MemoryLayout.structLayout(
                Clarabel_h.C_POINTER.withName("genpow_cone_alpha_t"),
                Clarabel_h.C_LONG_LONG.withName("genpow_cone_dim1_t"),
                Clarabel_h.C_LONG_LONG.withName("genpow_cone_dim2_t")
            ).withName("$anon$83:9")
        ).withName("$anon$64:5")
    ).withName("ClarabelSupportedConeT_f64");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt tag$LAYOUT = (OfInt)$LAYOUT.select(groupElement("tag"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * ClarabelSupportedConeT_Tag tag
     * }
     */
    public static final OfInt tag$layout() {
        return tag$LAYOUT;
    }

    private static final long tag$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * ClarabelSupportedConeT_Tag tag
     * }
     */
    public static final long tag$offset() {
        return tag$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * ClarabelSupportedConeT_Tag tag
     * }
     */
    public static int tag(MemorySegment struct) {
        return struct.get(tag$LAYOUT, tag$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * ClarabelSupportedConeT_Tag tag
     * }
     */
    public static void tag(MemorySegment struct, int fieldValue) {
        struct.set(tag$LAYOUT, tag$OFFSET, fieldValue);
    }

    private static final OfLong zero_cone_t$LAYOUT = (OfLong)$LAYOUT.select(groupElement("$anon$64:5"), groupElement("$anon$66:9"), groupElement("zero_cone_t"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uintptr_t zero_cone_t
     * }
     */
    public static final OfLong zero_cone_t$layout() {
        return zero_cone_t$LAYOUT;
    }

    private static final long zero_cone_t$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uintptr_t zero_cone_t
     * }
     */
    public static final long zero_cone_t$offset() {
        return zero_cone_t$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uintptr_t zero_cone_t
     * }
     */
    public static long zero_cone_t(MemorySegment struct) {
        return struct.get(zero_cone_t$LAYOUT, zero_cone_t$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uintptr_t zero_cone_t
     * }
     */
    public static void zero_cone_t(MemorySegment struct, long fieldValue) {
        struct.set(zero_cone_t$LAYOUT, zero_cone_t$OFFSET, fieldValue);
    }

    private static final OfLong nonnegative_cone_t$LAYOUT = (OfLong)$LAYOUT.select(groupElement("$anon$64:5"), groupElement("$anon$70:9"), groupElement("nonnegative_cone_t"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uintptr_t nonnegative_cone_t
     * }
     */
    public static final OfLong nonnegative_cone_t$layout() {
        return nonnegative_cone_t$LAYOUT;
    }

    private static final long nonnegative_cone_t$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uintptr_t nonnegative_cone_t
     * }
     */
    public static final long nonnegative_cone_t$offset() {
        return nonnegative_cone_t$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uintptr_t nonnegative_cone_t
     * }
     */
    public static long nonnegative_cone_t(MemorySegment struct) {
        return struct.get(nonnegative_cone_t$LAYOUT, nonnegative_cone_t$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uintptr_t nonnegative_cone_t
     * }
     */
    public static void nonnegative_cone_t(MemorySegment struct, long fieldValue) {
        struct.set(nonnegative_cone_t$LAYOUT, nonnegative_cone_t$OFFSET, fieldValue);
    }

    private static final OfLong second_order_cone_t$LAYOUT = (OfLong)$LAYOUT.select(groupElement("$anon$64:5"), groupElement("$anon$74:9"), groupElement("second_order_cone_t"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uintptr_t second_order_cone_t
     * }
     */
    public static final OfLong second_order_cone_t$layout() {
        return second_order_cone_t$LAYOUT;
    }

    private static final long second_order_cone_t$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uintptr_t second_order_cone_t
     * }
     */
    public static final long second_order_cone_t$offset() {
        return second_order_cone_t$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uintptr_t second_order_cone_t
     * }
     */
    public static long second_order_cone_t(MemorySegment struct) {
        return struct.get(second_order_cone_t$LAYOUT, second_order_cone_t$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uintptr_t second_order_cone_t
     * }
     */
    public static void second_order_cone_t(MemorySegment struct, long fieldValue) {
        struct.set(second_order_cone_t$LAYOUT, second_order_cone_t$OFFSET, fieldValue);
    }

    private static final OfDouble power_cone_t$LAYOUT = (OfDouble)$LAYOUT.select(groupElement("$anon$64:5"), groupElement("$anon$79:9"), groupElement("power_cone_t"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double power_cone_t
     * }
     */
    public static final OfDouble power_cone_t$layout() {
        return power_cone_t$LAYOUT;
    }

    private static final long power_cone_t$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double power_cone_t
     * }
     */
    public static final long power_cone_t$offset() {
        return power_cone_t$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double power_cone_t
     * }
     */
    public static double power_cone_t(MemorySegment struct) {
        return struct.get(power_cone_t$LAYOUT, power_cone_t$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double power_cone_t
     * }
     */
    public static void power_cone_t(MemorySegment struct, double fieldValue) {
        struct.set(power_cone_t$LAYOUT, power_cone_t$OFFSET, fieldValue);
    }

    private static final AddressLayout genpow_cone_alpha_t$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("$anon$64:5"), groupElement("$anon$83:9"), groupElement("genpow_cone_alpha_t"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * double *genpow_cone_alpha_t
     * }
     */
    public static final AddressLayout genpow_cone_alpha_t$layout() {
        return genpow_cone_alpha_t$LAYOUT;
    }

    private static final long genpow_cone_alpha_t$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * double *genpow_cone_alpha_t
     * }
     */
    public static final long genpow_cone_alpha_t$offset() {
        return genpow_cone_alpha_t$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * double *genpow_cone_alpha_t
     * }
     */
    public static MemorySegment genpow_cone_alpha_t(MemorySegment struct) {
        return struct.get(genpow_cone_alpha_t$LAYOUT, genpow_cone_alpha_t$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * double *genpow_cone_alpha_t
     * }
     */
    public static void genpow_cone_alpha_t(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(genpow_cone_alpha_t$LAYOUT, genpow_cone_alpha_t$OFFSET, fieldValue);
    }

    private static final OfLong genpow_cone_dim1_t$LAYOUT = (OfLong)$LAYOUT.select(groupElement("$anon$64:5"), groupElement("$anon$83:9"), groupElement("genpow_cone_dim1_t"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim1_t
     * }
     */
    public static final OfLong genpow_cone_dim1_t$layout() {
        return genpow_cone_dim1_t$LAYOUT;
    }

    private static final long genpow_cone_dim1_t$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim1_t
     * }
     */
    public static final long genpow_cone_dim1_t$offset() {
        return genpow_cone_dim1_t$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim1_t
     * }
     */
    public static long genpow_cone_dim1_t(MemorySegment struct) {
        return struct.get(genpow_cone_dim1_t$LAYOUT, genpow_cone_dim1_t$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim1_t
     * }
     */
    public static void genpow_cone_dim1_t(MemorySegment struct, long fieldValue) {
        struct.set(genpow_cone_dim1_t$LAYOUT, genpow_cone_dim1_t$OFFSET, fieldValue);
    }

    private static final OfLong genpow_cone_dim2_t$LAYOUT = (OfLong)$LAYOUT.select(groupElement("$anon$64:5"), groupElement("$anon$83:9"), groupElement("genpow_cone_dim2_t"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim2_t
     * }
     */
    public static final OfLong genpow_cone_dim2_t$layout() {
        return genpow_cone_dim2_t$LAYOUT;
    }

    private static final long genpow_cone_dim2_t$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim2_t
     * }
     */
    public static final long genpow_cone_dim2_t$offset() {
        return genpow_cone_dim2_t$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim2_t
     * }
     */
    public static long genpow_cone_dim2_t(MemorySegment struct) {
        return struct.get(genpow_cone_dim2_t$LAYOUT, genpow_cone_dim2_t$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uintptr_t genpow_cone_dim2_t
     * }
     */
    public static void genpow_cone_dim2_t(MemorySegment struct, long fieldValue) {
        struct.set(genpow_cone_dim2_t$LAYOUT, genpow_cone_dim2_t$OFFSET, fieldValue);
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

