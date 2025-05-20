import ru.leti.wise.task.plugin.graph.GraphCharacteristic;
import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.graph.model.Vertex;
import ru.leti.wise.task.graph.model.Edge;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

//
//В теории графов теорема Визинга утверждает, что любой простой неориентированный граф можно раскрасить
//рёбрами с помощью количества цветов, которое не более чем на единицу превышает максимальную степень Δ графа.
//Всегда требуется не менее Δ цветов, поэтому неориентированные графы можно разделить на два класса: графы «первого класса»,
//для которых достаточно Δ цветов, и графы «второго класса», для которых требуется Δ + 1 цвет. Более общая версия теоремы Визинга утверждает,
//что любой неориентированный мультиграф без петель можно раскрасить не более чем Δ+µ цветами, где µ — мультипликативность мультиграфа.[1]
//Но в нашем случае мы не рассматриваем мультиграфы.
//Теорема названа в честь Вадима Г. Визинга, который опубликовал её в 1964 году.

public class EdgeColoring implements GraphCharacteristic {
    @Override

    public int run(Graph graph) {
        // Проверка на пустой граф
        if (graph.getVertexList().isEmpty() || graph.getEdgeList().isEmpty()) {
            return 0;
        }

        // Делаем граф неориентированным, тк мультиграфы мы не рассматриваем, а ориентация не влияет на хроматическое число
        List<Edge> normalizedEdges = normalizeEdges(graph.getEdgeList());

        // Находим вершину с максимальной степенью
        int maxDegree = findMaxVertexDegree(graph, normalizedEdges);

        // Пробуем раскрасить рёбра с maxDegree цветами
        if (canColorEdges(graph, maxDegree, normalizedEdges)) {
            return maxDegree;
        }

        // Если не получилось, пробуем с maxDegree + 1 цветами
        return canColorEdges(graph, maxDegree+1, normalizedEdges) ? maxDegree+1 : -1;
    }

    // Нормализует ребра. Удаляет дубликаты (мультиграфы не рассматриваются), Упорядочиваются вершины
    public static List<Edge> normalizeEdges(List<Edge> edges) {
        // Используем Set для автоматического удаления дубликатов
        Set<Edge> uniqueEdges = new HashSet<>();

        for (Edge edge : edges) {
            // Нормализуем ребро: упорядочиваем вершины
            int source = Math.min(edge.getSource(), edge.getTarget());
            int target = Math.max(edge.getSource(), edge.getTarget());

            // Создаём новое ребро с нормализованными вершинами
            Edge normalizedEdge = new Edge(source, target, edge.getColor(), edge.getWeight(), edge.getLabel());

            uniqueEdges.add(normalizedEdge);
        }

        return new ArrayList<>(uniqueEdges);
    }

    // Находит максимальную степень вершины в графе.
    private int findMaxVertexDegree(Graph graph, List<Edge> normalizedEdges) {
        // ключ - id вершины, значение - степень вершины
        Map<Integer, Integer> vertexDegreeMap = new HashMap<>();

        //инициализируем нулями
        for (Vertex vertex : graph.getVertexList()) {
            vertexDegreeMap.put(vertex.getId(), 0);
        }

        // перебираем все ребра и считаем степени вершин
        for (Edge edge : normalizedEdges) {
            int sourceId = edge.getSource();
            vertexDegreeMap.put(sourceId, vertexDegreeMap.get(sourceId) + 1);
            int targetId = edge.getTarget();
            if (sourceId != targetId) {
                vertexDegreeMap.put(targetId, vertexDegreeMap.get(targetId) + 1);
            }
        }

        // Находим максимальную степень
        return vertexDegreeMap.values().stream()
                .max(Integer::compare)
                .orElse(0);
    }


    //Проверяет возможность раскраски рёбер графа заданным количеством цветов.
    private boolean canColorEdges(Graph graph, int numColors, List<Edge> normalizedEdges) {
        //List<Edge> edges = new ArrayList<>(graph.getEdgeList());
        List<Vertex> vertices = graph.getVertexList();

        // Если цветов нет и рёбер нет - можно раскрасить
        if (numColors == 0) {
            return normalizedEdges.isEmpty();
        }

        // Для каждой вершины храним набор используемых цветов
        HashMap<Integer, List<Integer>> vertexColors = new HashMap<Integer, List<Integer>>();
        for (Vertex vertex : vertices) {
            List<Integer> colors = new ArrayList<Integer>();
            vertexColors.put(vertex.getId(), colors);
        }

        return backtrackColoring(normalizedEdges, vertexColors, numColors, 0);
    }

    // Рекурсивный метод backtracking для проверки возможности раскраски

    private boolean backtrackColoring(List<Edge> edges,
                                      HashMap<Integer, List<Integer>> vertexColors,
                                      int numColors, int currentEdgeIndex) {
        // Все рёбра раскрашены - успех
        if (currentEdgeIndex == edges.size()) {

            return true;
        }

        Edge edge = edges.get(currentEdgeIndex);
        int u = edge.getSource();
        int v = edge.getTarget();

        // Находим доступные цвета для текущего ребра
        List<Integer> availableColors = new ArrayList<>();
        for (int color = 1; color <= numColors; color++) {
            if (!vertexColors.get(u).contains(color) &&
                    !vertexColors.get(v).contains(color)) {
                availableColors.add(color);
            }
        }

        // Пробуем каждый доступный цвет
        for (int color : availableColors) {
            // Применяем цвет
            vertexColors.get(u).add(color);
            vertexColors.get(v).add(color);

            // Рекурсивно проверяем оставшиеся рёбра
            if (backtrackColoring(edges, vertexColors, numColors, currentEdgeIndex + 1)) {
                return true;
            }

            // Откатываем изменения (backtrack)
            vertexColors.get(u).remove(Integer.valueOf(color));
            vertexColors.get(v).remove(Integer.valueOf(color));
        }

        return false;
    }


}
