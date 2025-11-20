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
    // Helper: inicializar contadores de hits/sinks via reflection
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
            throw new RuntimeException("Falha a inicializar countHits/countSinks por reflection", e);
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

        // estes dois são inicializados no construtor
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());

        // getHits/getSunkShips não são chamados aqui porque os Integer internos ainda podem ser null
    }

    // ------------------------------------------------------------
    // fire: tiro inválido (fora do tabuleiro, row < 0)
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire incrementa invalidShots para disparos fora do tabuleiro (linha negativa)")
    void fireCountsInvalidShots() {
        IPosition invalid = pos(-1, 0); // linha negativa → inválido

        IShip result = game.fire(invalid);

        assertNull(result);
        assertEquals(1, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertTrue(game.getShots().isEmpty());
    }

    // ------------------------------------------------------------
    // fire: tiro inválido (fora do tabuleiro, row > BOARD_SIZE)
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire incrementa invalidShots para disparos com coordenada maior que BOARD_SIZE")
    void fireCountsInvalidShotsAboveBoard() {
        // Fleet.BOARD_SIZE é 10 — uma posição com row = 11 é inválida
        IPosition invalid = pos(IFleet.BOARD_SIZE + 1, 0);

        IShip result = game.fire(invalid);

        assertNull(result);
        assertEquals(1, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertTrue(game.getShots().isEmpty());
    }

    // ------------------------------------------------------------
    // fire: tiro válido que falha (miss)
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire regista um tiro válido que falha (nenhum navio na posição)")
    void fireValidMiss() {
        IPosition p = pos(0, 0); // dentro do tabuleiro; fleet vazia

        IShip result = game.fire(p);

        assertNull(result);
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertEquals(1, game.getShots().size());
        assertEquals(p, game.getShots().get(0));
    }

    // ------------------------------------------------------------
    // fire: repeatedShot usando duas instâncias diferentes mas iguais
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire conta repeatedShots quando a mesma coordenada é disparada duas vezes (instâncias diferentes)")
    void fireCountsRepeatedShotsDifferentInstanceEquals() {
        IPosition p1 = pos(2, 2);
        IPosition p2 = new Position(2, 2); // nova instância com mesmas coordenadas

        game.fire(p1);        // primeiro tiro válido
        IShip result = game.fire(p2);  // segundo tiro na mesma posição (equals deve ser true)

        assertNull(result);
        assertEquals(0, game.getInvalidShots());
        assertEquals(1, game.getRepeatedShots());
        assertEquals(1, game.getShots().size()); // só guardou uma vez
        assertEquals(p1, game.getShots().get(0));
    }

    // ------------------------------------------------------------
    // fire: tiro num limite (row/col == BOARD_SIZE) — o código actual trata como válido
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire considera válido um tiro em (BOARD_SIZE, BOARD_SIZE) dado o <= presente no código")
    void fireAcceptsBoardSizeCoordinateAsValid() {
        IPosition boundary = pos(IFleet.BOARD_SIZE, IFleet.BOARD_SIZE);

        IShip result = game.fire(boundary);

        // É tratado como válido pelo método validShot actual
        assertNull(result);
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertEquals(1, game.getShots().size());
        assertEquals(boundary, game.getShots().get(0));
    }

    // ------------------------------------------------------------
    // fire: hit mas navio ainda a flutuar
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire conta hit quando acerta num navio que ainda flutua, sem aumentar sunkShips")
    void fireHitButNotSunk() {
        initHitAndSinkCounters();

        // Caravel tem SIZE = 2 → não afunda com um único tiro
        Caravel caravel = new Caravel(Compass.EAST, pos(2, 2));
        assertTrue(fleet.addShip(caravel));

        IPosition alvo = caravel.getPositions().get(0);

        IShip result = game.fire(alvo);

        assertNull(result);
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertEquals(1, game.getHits());
        assertEquals(0, game.getSunkShips());
        assertEquals(1, game.getShots().size());
        assertTrue(caravel.stillFloating());
    }

    // ------------------------------------------------------------
    // fire: hit e navio afundado
    // ------------------------------------------------------------

    @Test
    @DisplayName("fire conta hit e sunkShips quando acerta numa barca (afunda com 1 tiro)")
    void fireHitAndSinksShip() {
        initHitAndSinkCounters();

        Barge barge = new Barge(Compass.NORTH, pos(5, 5));
        assertTrue(fleet.addShip(barge));

        IShip result = game.fire(pos(5, 5));

        assertSame(barge, result);
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertEquals(1, game.getHits());
        assertEquals(1, game.getSunkShips());
        assertEquals(1, game.getShots().size());
        assertFalse(barge.stillFloating());
    }

    // ------------------------------------------------------------
    // getRemainingShips
    // ------------------------------------------------------------

    @Test
    @DisplayName("getRemainingShips devolve número de navios ainda a flutuar")
    void getRemainingShipsUsesFleet() {
        initHitAndSinkCounters();

        Barge b1 = new Barge(Compass.NORTH, pos(0, 0));
        Barge b2 = new Barge(Compass.NORTH, pos(2, 2));
        assertTrue(fleet.addShip(b1));
        assertTrue(fleet.addShip(b2));

        assertEquals(2, game.getRemainingShips());

        // afundar um deles
        game.fire(pos(0, 0));

        assertEquals(1, game.getRemainingShips());
    }

    // ------------------------------------------------------------
    // printBoard / printValidShots / printFleet – garantir que não lançam exceção
    // ------------------------------------------------------------

    @Test
    @DisplayName("printBoard imprime um mapa com as posições marcadas sem lançar exceção")
    void printBoardDoesNotThrow() {
        List<IPosition> positions = List.of(
                pos(0, 0),
                pos(IFleet.BOARD_SIZE - 1, IFleet.BOARD_SIZE - 1)
        );

        assertDoesNotThrow(() -> game.printBoard(positions, 'X'));
    }

    @Test
    @DisplayName("printValidShots imprime os tiros válidos sem lançar exceção")
    void printValidShotsDoesNotThrow() {
        // usa posições normais (0..BOARD_SIZE-1) para evitar ArrayIndexOutOfBounds em printBoard
        game.fire(pos(0, 0));
        game.fire(pos(1, 1));

        assertDoesNotThrow(() -> game.printValidShots());
    }

    @Test
    @DisplayName("printFleet imprime as posições dos navios da fleet sem lançar exceção")
    void printFleetDoesNotThrow() {
        assertTrue(fleet.addShip(new Barge(Compass.NORTH, pos(0, 0))));
        assertTrue(fleet.addShip(new Caravel(Compass.EAST, pos(3, 3))));

        assertDoesNotThrow(() -> game.printFleet());
    }

}
