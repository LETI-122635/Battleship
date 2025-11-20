package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FrigateTest {

    @Test
    @DisplayName("Frigate NORTH cria 4 posições verticais a partir da posição inicial")
    void constructorNorthCreatesVerticalPositions() {
        IPosition start = new Position(2, 3);

        Frigate frigate = new Frigate(Compass.NORTH, start);

        List<IPosition> positions = frigate.getPositions();
        assertEquals(4, positions.size(), "Frigate deve ocupar 4 casas");

        for (int i = 0; i < 4; i++) {
            IPosition p = positions.get(i);
            assertEquals(2 + i, p.getRow(), "linha deve aumentar de forma incremental (NORTH)");
            assertEquals(3, p.getColumn(), "coluna deve ser constante (NORTH)");
        }

        assertEquals("Fragata", frigate.getCategory());
        assertEquals(Compass.NORTH, frigate.getBearing());
        assertEquals(Integer.valueOf(4), frigate.getSize());
    }

    @Test
    @DisplayName("Frigate SOUTH cria 4 posições verticais a partir da posição inicial")
    void constructorSouthCreatesVerticalPositions() {
        IPosition start = new Position(5, 1);

        Frigate frigate = new Frigate(Compass.SOUTH, start);

        List<IPosition> positions = frigate.getPositions();
        assertEquals(4, positions.size());

        for (int i = 0; i < 4; i++) {
            IPosition p = positions.get(i);
            assertEquals(5 + i, p.getRow(), "linha deve aumentar de forma incremental (SOUTH)");
            assertEquals(1, p.getColumn(), "coluna deve ser constante (SOUTH)");
        }

        assertEquals(Compass.SOUTH, frigate.getBearing());
    }

    @Test
    @DisplayName("Frigate EAST cria 4 posições horizontais a partir da posição inicial")
    void constructorEastCreatesHorizontalPositions() {
        IPosition start = new Position(4, 2);

        Frigate frigate = new Frigate(Compass.EAST, start);

        List<IPosition> positions = frigate.getPositions();
        assertEquals(4, positions.size());

        for (int i = 0; i < 4; i++) {
            IPosition p = positions.get(i);
            assertEquals(4, p.getRow(), "linha deve ser constante (EAST)");
            assertEquals(2 + i, p.getColumn(), "coluna deve aumentar de forma incremental (EAST)");
        }

        assertEquals(Compass.EAST, frigate.getBearing());
    }

    @Test
    @DisplayName("Frigate WEST cria 4 posições horizontais a partir da posição inicial")
    void constructorWestCreatesHorizontalPositions() {
        IPosition start = new Position(7, 6);

        Frigate frigate = new Frigate(Compass.WEST, start);

        List<IPosition> positions = frigate.getPositions();
        assertEquals(4, positions.size());

        for (int i = 0; i < 4; i++) {
            IPosition p = positions.get(i);
            assertEquals(7, p.getRow(), "linha deve ser constante (WEST)");
            // implementação actual também usa +c para WEST
            assertEquals(6 + i, p.getColumn(), "coluna deve aumentar de forma incremental (WEST)");
        }

        assertEquals(Compass.WEST, frigate.getBearing());
    }

    @Test
    @DisplayName("getSize devolve sempre 4 e consistente com o número de posições")
    void sizeIsAlwaysFour() {
        Frigate frigate = new Frigate(Compass.NORTH, new Position(0, 0));

        assertEquals(Integer.valueOf(4), frigate.getSize());
        assertEquals(4, frigate.getPositions().size());
    }
}
