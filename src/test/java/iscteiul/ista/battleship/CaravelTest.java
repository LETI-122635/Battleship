package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaravelTest {

    @Test
    @DisplayName("Caravel NORTH cria 2 posições verticais a partir da posição inicial")
    void constructorNorthCreatesVerticalPositions() {
        IPosition start = new Position(2, 3);

        Caravel caravel = new Caravel(Compass.NORTH, start);

        List<IPosition> positions = caravel.getPositions();
        assertEquals(2, positions.size(), "Caravel deve ocupar 2 casas");

        for (int i = 0; i < 2; i++) {
            IPosition p = positions.get(i);
            assertEquals(2 + i, p.getRow(), "linha deve aumentar (NORTH)");
            assertEquals(3, p.getColumn(), "coluna constante (NORTH)");
        }

        assertEquals("Caravela", caravel.getCategory());
        assertEquals(Compass.NORTH, caravel.getBearing());
        assertEquals(Integer.valueOf(2), caravel.getSize());
    }

    @Test
    @DisplayName("Caravel SOUTH cria 2 posições verticais a partir da posição inicial")
    void constructorSouthCreatesVerticalPositions() {
        IPosition start = new Position(5, 1);

        Caravel caravel = new Caravel(Compass.SOUTH, start);

        List<IPosition> positions = caravel.getPositions();
        assertEquals(2, positions.size());

        for (int i = 0; i < 2; i++) {
            IPosition p = positions.get(i);
            assertEquals(5 + i, p.getRow(), "linha deve aumentar (SOUTH)");
            assertEquals(1, p.getColumn(), "coluna constante (SOUTH)");
        }

        assertEquals(Compass.SOUTH, caravel.getBearing());
    }

    @Test
    @DisplayName("Caravel EAST cria 2 posições horizontais a partir da posição inicial")
    void constructorEastCreatesHorizontalPositions() {
        IPosition start = new Position(4, 2);

        Caravel caravel = new Caravel(Compass.EAST, start);

        List<IPosition> positions = caravel.getPositions();
        assertEquals(2, positions.size());

        for (int i = 0; i < 2; i++) {
            IPosition p = positions.get(i);
            assertEquals(4, p.getRow(), "linha constante (EAST)");
            assertEquals(2 + i, p.getColumn(), "coluna deve aumentar (EAST)");
        }

        assertEquals(Compass.EAST, caravel.getBearing());
    }

    @Test
    @DisplayName("Caravel WEST cria 2 posições horizontais a partir da posição inicial")
    void constructorWestCreatesHorizontalPositions() {
        IPosition start = new Position(7, 6);

        Caravel caravel = new Caravel(Compass.WEST, start);

        List<IPosition> positions = caravel.getPositions();
        assertEquals(2, positions.size());

        for (int i = 0; i < 2; i++) {
            IPosition p = positions.get(i);
            assertEquals(7, p.getRow(), "linha constante (WEST)");
            // pela implementação, WEST também soma +c
            assertEquals(6 + i, p.getColumn(), "coluna deve aumentar (WEST)");
        }

        assertEquals(Compass.WEST, caravel.getBearing());
    }

    @Test
    @DisplayName("getSize devolve sempre 2 e coincide com o número de posições")
    void sizeIsAlwaysTwo() {
        Caravel caravel = new Caravel(Compass.NORTH, new Position(0, 0));

        assertEquals(Integer.valueOf(2), caravel.getSize());
        assertEquals(2, caravel.getPositions().size());
    }
}
