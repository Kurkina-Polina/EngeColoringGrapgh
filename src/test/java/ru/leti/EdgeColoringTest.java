package ru.leti;
import ru.leti.EdgeColoring;
import ru.leti.wise.task.graph.util.FileLoader;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EdgeColoringTest {
    @Test
    public void coloringTest() throws FileNotFoundException {
        EdgeColoring edgecoloring = new EdgeColoring();
        var graph1 = FileLoader.loadGraphFromJson("src/test/resources/graph1.json");
        assertThat(edgecoloring.run(graph1) == 7).isTrue();
    }
}
