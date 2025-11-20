package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Fleet using local test doubles (TestShip / TestPosition).
 * These doubles implement the methods that Fleet calls on IShip and IPosition,
 * so the tests do not depend on the rest of the project.
 */
class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    @Test
    @DisplayName("constants BOARD_SIZE and FLEET_SIZE")
    void constants() {
        assertEquals(10, IFleet.BOARD_SIZE);
        assertEquals(10, IFleet.FLEET_SIZE);
    }

    @Test
    @DisplayName("new fleet starts empty")
    void newFleetStartsEmpty() {
        assertNotNull(fleet.getShips());
        assertTrue(fleet.getShips().isEmpty());
        assertNotNull(fleet.getShipsLike("any"));
        assertTrue(fleet.getShipsLike("any").isEmpty());
        assertNotNull(fleet.getFloatingShips());
        assertTrue(fleet.getFloatingShips().isEmpty());
        assertNull(fleet.shipAt(new TestPosition(0, 0)));
    }

    @Test
    @DisplayName("addShip: success for valid ship inside board with no collision")
    void addShipSuccess() {
        TestShip s = new TestShip("Fragata", true, false,
                pos(2, 2), pos(2, 3));
        assertTrue(fleet.addShip(s));
        assertEquals(1, fleet.getShips().size());
    }

    @Test
    @DisplayName("addShip: fail when leftMostPos < 0 (outside board)")
    void addShipFailsLeftOut() {
        TestShip outsideLeft = new TestShip("Nau", true, false,
                new TestPosition(0, -1),
                new TestPosition(0, 0));
        assertFalse(fleet.addShip(outsideLeft));
    }

    @Test
    @DisplayName("addShip: fail when rightMostPos > BOARD_SIZE - 1")
    void addShipFailsRightOut() {
        TestShip outsideRight = new TestShip("Nau", true, false,
                new TestPosition(0, 8),
                new TestPosition(0, 10)); // rightMost = 10 > 9
        assertFalse(fleet.addShip(outsideRight));
    }

    @Test
    @DisplayName("addShip: fail when topMostPos < 0")
    void addShipFailsTopOut() {
        TestShip outsideTop = new TestShip("Nau", true, false,
                new TestPosition(-1, 5),
                new TestPosition(0, 5));
        assertFalse(fleet.addShip(outsideTop));
    }

    @Test
    @DisplayName("addShip: fail when bottomMostPos > BOARD_SIZE - 1")
    void addShipFailsBottomOut() {
        TestShip outsideBottom = new TestShip("Nau", true, false,
                new TestPosition(9, 5),
                new TestPosition(10, 5)); // bottomMost = 10 > 9
        assertFalse(fleet.addShip(outsideBottom));
    }

    @Test
    @DisplayName("addShip: fail when there is collision risk")
    void addShipFailsCollision() {
        // existing ship will report tooCloseTo==true
        TestShip a = new TestShip("Galeao", true, true, pos(1, 1));
        assertTrue(fleet.addShip(a));

        // new ship is valid but should be rejected due to collisionRisk
        TestShip b = new TestShip("Galeao", true, false, pos(5, 5));
        assertFalse(fleet.addShip(b));
    }

    @Test
    @DisplayName("getShipsLike filters by category")
    void getShipsLikeFilters() {
        TestShip g = new TestShip("Galeao", true, false, pos(0, 0));
        TestShip f = new TestShip("Fragata", true, false, pos(1, 0));
        fleet.addShip(g);
        fleet.addShip(f);

        List<IShip> galeoes = fleet.getShipsLike("Galeao");
        assertEquals(1, galeoes.size());
        assertSame(g, galeoes.get(0));

        assertTrue(fleet.getShipsLike("Nau").isEmpty());
    }

    @Test
    @DisplayName("getFloatingShips returns only ships still floating")
    void getFloatingShipsReturnsOnlyFloating() {
        TestShip floating = new TestShip("Fragata", true, false, pos(0, 0));
        TestShip sunk = new TestShip("Fragata", false, false, pos(1, 0));
        fleet.addShip(floating);
        fleet.addShip(sunk);

        List<IShip> floatingShips = fleet.getFloatingShips();
        assertEquals(1, floatingShips.size());
        assertSame(floating, floatingShips.get(0));
    }

    @Test
    @DisplayName("shipAt returns correct ship or null")
    void shipAtBehaviour() {
        TestShip s = new TestShip("Barca", true, false, pos(4, 4), pos(4, 5));
        fleet.addShip(s);

        assertSame(s, fleet.shipAt(pos(4, 4)));
        assertSame(s, fleet.shipAt(pos(4, 5)));
        assertNull(fleet.shipAt(pos(0, 0)));
    }

    @Test
    @DisplayName("printing helpers do not throw")
    void printingHelpersDoNotThrow() {
        fleet.addShip(new TestShip("Fragata", true, false, pos(0, 0)));
        fleet.addShip(new TestShip("Galeao", true, false, pos(1, 0)));

        assertDoesNotThrow(() -> fleet.printAllShips());
        assertDoesNotThrow(() -> fleet.printFloatingShips());
        assertDoesNotThrow(() -> fleet.printShipsByCategory("Galeao"));
        assertDoesNotThrow(() -> fleet.printStatus());
    }

    @Test
    @DisplayName("printShipsByCategory throws AssertionError on null category")
    void printShipsByCategoryNull() {
        assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null));
    }

    // -----------------------
    // Test doubles
    // -----------------------

    private static TestPosition pos(int row, int col) {
        return new TestPosition(row, col);
    }

    /**
     * Minimal test double implementing IPosition.
     * If your IPosition has more methods, add trivial implementations here.
     */
    private static class TestPosition implements IPosition {
        final int row;
        final int col;
        private boolean occupied = false;
        private boolean hit = false;

        TestPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int getRow() {
            return row;
        }

        // some codebases call getColumn(), others getCol(). If compiler complains, adapt.
        @Override
        public int getColumn() {
            return col;
        }

        @Override
        public boolean isAdjacentTo(IPosition other) {
            if (!(other instanceof TestPosition o)) return false;
            int dr = Math.abs(row - o.row);
            int dc = Math.abs(col - o.col);
            return (dr <= 1 && dc <= 1) && (dr + dc > 0);
        }

        @Override
        public void occupy() {
            occupied = true;
        }

        @Override
        public void shoot() {
            hit = true;
        }

        @Override
        public boolean isOccupied() {
            return occupied;
        }

        @Override
        public boolean isHit() {
            return hit;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestPosition)) return false;
            TestPosition that = (TestPosition) o;
            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public String toString() {
            return "(" + row + "," + col + ")";
        }
    }

    /**
     * Minimal test double implementing IShip.
     * Implements the methods used by Fleet.
     * If your IShip declares more methods, add them as trivial operations here.
     */
    private static class TestShip implements IShip {
        private final String category;
        private final boolean floating;
        private final boolean collidesWithAny;
        private final List<TestPosition> positions;
        private final int left;
        private final int right;
        private final int top;
        private final int bottom;

        TestShip(String category, boolean floating, boolean collidesWithAny, TestPosition... positions) {
            this.category = category;
            this.floating = floating;
            this.collidesWithAny = collidesWithAny;
            this.positions = new ArrayList<>();
            int minRow = Integer.MAX_VALUE, maxRow = Integer.MIN_VALUE;
            int minCol = Integer.MAX_VALUE, maxCol = Integer.MIN_VALUE;

            for (TestPosition p : positions) {
                this.positions.add(p);
                minRow = Math.min(minRow, p.row);
                maxRow = Math.max(maxRow, p.row);
                minCol = Math.min(minCol, p.col);
                maxCol = Math.max(maxCol, p.col);
            }

            if (this.positions.isEmpty()) {
                // default single cell at (0,0) if none provided
                this.positions.add(new TestPosition(0, 0));
                minRow = maxRow = 0;
                minCol = maxCol = 0;
            }

            this.top = minRow;
            this.bottom = maxRow;
            this.left = minCol;
            this.right = maxCol;
        }

        // methods used by Fleet

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public Integer getSize() {
            return positions.size();
        }

        @Override
        public List<IPosition> getPositions() {
            return new ArrayList<>(positions);
        }

        @Override
        public IPosition getPosition() {
            return positions.isEmpty() ? null : positions.get(0);
        }

        @Override
        public Compass getBearing() {
            return Compass.NORTH; // not relevant for Fleet
        }

        @Override
        public boolean stillFloating() {
            return floating;
        }

        @Override
        public int getTopMostPos() {
            return top;
        }

        @Override
        public int getBottomMostPos() {
            return bottom;
        }

        @Override
        public int getLeftMostPos() {
            return left;
        }

        @Override
        public int getRightMostPos() {
            return right;
        }

        @Override
        public boolean occupies(IPosition pos) {
            if (!(pos instanceof TestPosition)) return false;
            return positions.contains(pos);
        }

        @Override
        public boolean tooCloseTo(IShip other) {
            // simplified: if this ship was created with collidesWithAny = true, it collides with any other
            return collidesWithAny;
        }

        @Override
        public boolean tooCloseTo(IPosition pos) {
            // not used by Fleet, but implement trivially
            if (!(pos instanceof TestPosition p)) return false;
            for (TestPosition tp : positions) {
                if (tp.isAdjacentTo(p)) return true;
            }
            return false;
        }

        @Override
        public void shoot(IPosition pos) {
            if (!(pos instanceof TestPosition p)) return;
            for (TestPosition tp : positions) {
                if (tp.equals(p)) {
                    tp.shoot();
                }
            }
        }

        @Override
        public String toString() {
            return "TestShip{" + category + " " + positions + "}";
        }
    }
}
