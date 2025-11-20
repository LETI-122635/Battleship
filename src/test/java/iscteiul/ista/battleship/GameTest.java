package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Fleet fleet;
    private Game game;

    private Position pos(int row, int col) {
        return new Position(row, col);
    }

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
        game = new Game(fleet);
    }

    // ------------------------------------------------------------
    // Helper: inicializar countHits e countSinks (porque o código tem bug)
    // ------------------------------------------------------------

    private void initHitAndSinkCounters() {
        try {
            Field hits = Game.class.getDeclaredField("countHits");
            hits.setAccessible(true);
            hits.set(game, Integer.valueOf(0));

            Field sinks = Game.class.getDeclaredField("countSinks");
            sinks.setAccessible(true);
            sinks.set(game, Integer.valueOf(0));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar counters via reflection", e);
        }
    }

    // ------------------------------------------------------------
    // Estado inicial
    // ------------------------------------------------------------

    @Test
    @DisplayName("Novo Game começa com lista de shots vazia e contadores básicos a zero")
    void newGameStartsEmpty() {
        assertNotNull(game.getShots());
        assertTrue(game.getShots().isEmpty());
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
    }

    // ------------------------------------------------------------
    // validShot → testar: row < 0
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire incrementa invalidShots para row < 0")
    void fireInvalidRowNegative() {
        IPosition invalid = pos(-1, 0);

        IShip result = game.fire(invalid);

        assertNull(result);
        assertEquals(1, game.getInvalidShots());
        assertTrue(game.getShots().isEmpty());
    }

    // ------------------------------------------------------------
    // validShot → testar: row > BOARD_SIZE
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire incrementa invalidShots para row > BOARD_SIZE")
    void fireInvalidRowAboveBoard() {
        IPosition invalid = pos(IFleet.BOARD_SIZE + 1, 0);

        IShip result = game.fire(invalid);

        assertNull(result);
        assertEquals(1, game.getInvalidShots());
        assertTrue(game.getShots().isEmpty());
    }

    // ------------------------------------------------------------
    // validShot → testar: col < 0
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire incrementa invalidShots para col < 0")
    void fireInvalidColumnNegative() {
        IPosition invalid = pos(0, -1);

        IShip result = game.fire(invalid);

        assertNull(result);
        assertEquals(1, game.getInvalidShots());
        assertTrue(game.getShots().isEmpty());
    }

    // ------------------------------------------------------------
    // validShot → testar: col > BOARD_SIZE
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire incrementa invalidShots para col > BOARD_SIZE")
    void fireInvalidColumnAboveBoard() {
        IPosition invalid = pos(0, IFleet.BOARD_SIZE + 1);

        IShip result = game.fire(invalid);

        assertNull(result);
        assertEquals(1, game.getInvalidShots());
        assertTrue(game.getShots().isEmpty());
    }

    // ------------------------------------------------------------
    // validShot → testar limite da fronteira (BOARD_SIZE, BOARD_SIZE)
    // ------------------------------------------------------------

    @Test
    @DisplayName("Disparo em (BOARD_SIZE, BOARD_SIZE) é considerado válido pelo código actual")
    void fireAcceptsBoundaryCell() {
        IPosition boundary = pos(IFleet.BOARD_SIZE, IFleet.BOARD_SIZE);

        IShip result = game.fire(boundary);

        assertNull(result);
        assertEquals(0, game.getInvalidShots());
        assertEquals(1, game.getShots().size());
        assertEquals(boundary, game.getShots().get(0));
    }

    // ------------------------------------------------------------
    // fire: tiro válido que falha
    // ------------------------------------------------------------

    @Test
    @DisplayName("Tiro válido que falha é registado correctamente")
    void fireValidMiss() {
        IPosition p = pos(1, 1);

        IShip result = game.fire(p);

        assertNull(result);
        assertEquals(1, game.getShots().size());
        assertEquals(p, game.getShots().get(0));
    }

    // ------------------------------------------------------------
    // repeatedShot
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire detecta tiro repetido mesmo com instância diferente do mesmo ponto")
    void fireRepeatedShotDifferentInstance() {
        IPosition p1 = pos(3, 3);
        IPosition p2 = new Position(3, 3);

        game.fire(p1);
        IShip result = game.fire(p2);

        assertNull(result);
        assertEquals(1, game.getRepeatedShots());
        assertEquals(1, game.getShots().size());
    }

    // ------------------------------------------------------------
    // hit mas navio ainda flutua
    // ------------------------------------------------------------

    @Test
    @DisplayName("Hit num navio ainda a flutuar aumenta hits mas não sunkShips")
    void fireHitButNotSunk() {
        initHitAndSinkCounters();

        Caravel caravel = new Caravel(Compass.EAST, pos(2, 2));
        fleet.addShip(caravel);

        IPosition hit = caravel.getPositions().get(0);
        IShip result = game.fire(hit);

        assertNull(result);
        assertEquals(1, game.getHits());
        assertEquals(0, game.getSunkShips());
        assertTrue(caravel.stillFloating());
    }

    // ------------------------------------------------------------
    // hit e navio afundado
    // ------------------------------------------------------------

    @Test
    @DisplayName("Hit numa barca afunda imediatamente o navio")
    void fireHitAndSink() {
        initHitAndSinkCounters();

        Barge b = new Barge(Compass.NORTH, pos(5, 5));
        fleet.addShip(b);

        IShip result = game.fire(pos(5, 5));

        assertSame(b, result);
        assertEquals(1, game.getHits());
        assertEquals(1, game.getSunkShips());
        assertFalse(b.stillFloating());
    }

    // ------------------------------------------------------------
    // getRemainingShips
    // ------------------------------------------------------------

    @Test
    @DisplayName("getRemainingShips regressa número correcto após tiros")
    void getRemainingShips() {
        initHitAndSinkCounters();

        Barge b1 = new Barge(Compass.NORTH, pos(0, 0));
        Barge b2 = new Barge(Compass.NORTH, pos(2, 2));

        fleet.addShip(b1);
        fleet.addShip(b2);

        assertEquals(2, game.getRemainingShips());

        game.fire(pos(0, 0)); // afundar uma barca

        assertEquals(1, game.getRemainingShips());
    }

    // ------------------------------------------------------------
    // printing
    // ------------------------------------------------------------

    @Test
    @DisplayName("printBoard não lança exceção")
    void printBoardDoesNotThrow() {
        List<IPosition> pts = List.of(
                pos(0, 0),
                pos(IFleet.BOARD_SIZE - 1, IFleet.BOARD_SIZE - 1)
        );

        assertDoesNotThrow(() -> game.printBoard(pts, 'X'));
    }

    @Test
    @DisplayName("printValidShots não lança exceção")
    void printValidShotsDoesNotThrow() {
        game.fire(pos(1, 1));
        game.fire(pos(2, 2));

        assertDoesNotThrow(() -> game.printValidShots());
    }

    @Test
    @DisplayName("printFleet não lança exceção")
    void printFleetDoesNotThrow() {
        fleet.addShip(new Barge(Compass.NORTH, pos(0, 0)));
        fleet.addShip(new Caravel(Compass.EAST, pos(3, 3)));

        assertDoesNotThrow(() -> game.printFleet());
    }
}