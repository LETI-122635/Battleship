package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GalleonTest {

    @Test
    @DisplayName("Galleon NORTH cria forma correta a partir da posição inicial")
    void constructorNorthCreatesExpectedShape() {
        IPosition start = new Position(2, 3);

        Galleon galleon = new Galleon(Compass.NORTH, start);

        List<IPosition> positions = galleon.getPositions();
        assertEquals(5, positions.size(), "Galleon deve ocupar 5 casas");

        // fillNorth:
        // (r,c), (r,c+1), (r,c+2), (r+1,c+1), (r+2,c+1)
        assertEquals(2, positions.get(0).getRow());
        assertEquals(3, positions.get(0).getColumn());

        assertEquals(2, positions.get(1).getRow());
        assertEquals(4, positions.get(1).getColumn());

        assertEquals(2, positions.get(2).getRow());
        assertEquals(5, positions.get(2).getColumn());

        assertEquals(3, positions.get(3).getRow());
        assertEquals(4, positions.get(3).getColumn());

        assertEquals(4, positions.get(4).getRow());
        assertEquals(4, positions.get(4).getColumn());

        assertEquals("Galeao", galleon.getCategory());
        assertEquals(Compass.NORTH, galleon.getBearing());
        assertEquals(Integer.valueOf(5), galleon.getSize());
    }

    @Test
    @DisplayName("Galleon SOUTH cria forma correta a partir da posição inicial")
    void constructorSouthCreatesExpectedShape() {
        IPosition start = new Position(2, 2);

        Galleon galleon = new Galleon(Compass.SOUTH, start);

        List<IPosition> positions = galleon.getPositions();
        assertEquals(5, positions.size());

        // fillSouth:
        // (r,c), (r+1,c), (r+2,c-1), (r+2,c), (r+2,c+1)
        assertEquals(2, positions.get(0).getRow());
        assertEquals(2, positions.get(0).getColumn());

        assertEquals(3, positions.get(1).getRow());
        assertEquals(2, positions.get(1).getColumn());

        assertEquals(4, positions.get(2).getRow());
        assertEquals(1, positions.get(2).getColumn());

        assertEquals(4, positions.get(3).getRow());
        assertEquals(2, positions.get(3).getColumn());

        assertEquals(4, positions.get(4).getRow());
        assertEquals(3, positions.get(4).getColumn());

        assertEquals(Compass.SOUTH, galleon.getBearing());
    }

    @Test
    @DisplayName("Galleon EAST cria forma correta a partir da posição inicial")
    void constructorEastCreatesExpectedShape() {
        IPosition start = new Position(4, 3);

        Galleon galleon = new Galleon(Compass.EAST, start);

        List<IPosition> positions = galleon.getPositions();
        assertEquals(5, positions.size());

        // fillEast:
        // (r,c), (r+1,c-2), (r+1,c-1), (r+1,c), (r+2,c)
        assertEquals(4, positions.get(0).getRow());
        assertEquals(3, positions.get(0).getColumn());

        assertEquals(5, positions.get(1).getRow());
        assertEquals(1, positions.get(1).getColumn());

        assertEquals(5, positions.get(2).getRow());
        assertEquals(2, positions.get(2).getColumn());

        assertEquals(5, positions.get(3).getRow());
        assertEquals(3, positions.get(3).getColumn());

        assertEquals(6, positions.get(4).getRow());
        assertEquals(3, positions.get(4).getColumn());

        assertEquals(Compass.EAST, galleon.getBearing());
    }

    @Test
    @DisplayName("Galleon WEST cria forma correta a partir da posição inicial")
    void constructorWestCreatesExpectedShape() {
        IPosition start = new Position(4, 3);

        Galleon galleon = new Galleon(Compass.WEST, start);

        List<IPosition> positions = galleon.getPositions();
        assertEquals(5, positions.size());

        // fillWest:
        // (r,c), (r+1,c), (r+1,c+1), (r+1,c+2), (r+2,c)
        assertEquals(4, positions.get(0).getRow());
        assertEquals(3, positions.get(0).getColumn());

        assertEquals(5, positions.get(1).getRow());
        assertEquals(3, positions.get(1).getColumn());

        assertEquals(5, positions.get(2).getRow());
        assertEquals(4, positions.get(2).getColumn());

        assertEquals(5, positions.get(3).getRow());
        assertEquals(5, positions.get(3).getColumn());

        assertEquals(6, positions.get(4).getRow());
        assertEquals(3, positions.get(4).getColumn());

        assertEquals(Compass.WEST, galleon.getBearing());
    }

    @Test
    @DisplayName("getSize devolve sempre 5 e corresponde ao número de posições")
    void sizeIsAlwaysFive() {
        Galleon g1 = new Galleon(Compass.NORTH, new Position(0, 0));
        Galleon g2 = new Galleon(Compass.EAST, new Position(3, 3));

        assertEquals(Integer.valueOf(5), g1.getSize());
        assertEquals(5, g1.getPositions().size());

        assertEquals(Integer.valueOf(5), g2.getSize());
        assertEquals(5, g2.getPositions().size());
    }

    @Test
    @DisplayName("Construtor de Galleon com bearing null lança AssertionError (do Ship)")
    void constructorThrowsAssertionWhenBearingNull() {
        IPosition start = new Position(0, 0);

        assertThrows(AssertionError.class,
                () -> new Galleon(null, start));
    }

}
