import ru.leti.wise.task.graph.util.FileLoader;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdgeColoringTest {
    @Test
    public void coloringTest() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var graph1 = FileLoader.loadGraphFromJson("src/test/resources/graph1.json");
        assertThat(edgecoloring.run(graph1) == 7).isTrue();
    }
    @Test
    public void testEmptyGraph() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var empty_graph = FileLoader.loadGraphFromJson("src/test/resources/empty_graph.json");
        assertThat(edgecoloring.run(empty_graph) == 0).isTrue();

    }
    @Test
    public void testSingleEdgeGraph() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var graph_one_edge = FileLoader.loadGraphFromJson("src/test/resources/graph_one_edge.json");
        assertThat(edgecoloring.run(graph_one_edge) == 1).isTrue();
    }
    @Test
    public void testBipartiteGraph() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var graph_bipartite = FileLoader.loadGraphFromJson("src/test/resources/graph-bipartite.json");
        assertThat(edgecoloring.run(graph_bipartite) == 3).isTrue();
        //assertEquals(3, edgecoloring.run(graph_bipartite));
    }

    @Test
    public void testCycleGraph() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var graph_cycle = FileLoader.loadGraphFromJson("src/test/resources/graph-cycle.json");
        assertThat(edgecoloring.run(graph_cycle) == 2).isTrue();
        //assertEquals(2, edgecoloring.run(graph_cycle));
    }

    @Test
    public void testGraphLoop() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var graph_loop = FileLoader.loadGraphFromJson("src/test/resources/graph-with-petlya.json");
        assertThat(edgecoloring.run(graph_loop) == 2).isTrue();
        //assertEquals(2, edgecoloring.run(graph_loop));
    }

    @Test
    public void testGraphK5() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var graph_k5 = FileLoader.loadGraphFromJson("src/test/resources/graph-k5.json");
        //assertThat(edgecoloring.run(graph_k5) == 5).isTrue();
        assertEquals(5, edgecoloring.run(graph_k5));
    }


}
