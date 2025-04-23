package ru.leti;

import ru.leti.wise.task.plugin.graph.GraphCharacteristic;
import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.graph.model.Vertex;
import ru.leti.wise.task.graph.model.Edge;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class EdgeColoring implements GraphCharacteristic {
    @Override

    public int run(Graph graph) {
        // Проверка на пустой граф
        if (graph.getVertexList().isEmpty() || graph.getEdgeList().isEmpty()) {
            return 0;
        }

        // Находим вершину с максимальной степенью
        int maxDegree = findMaxVertexDegree(graph);

        // Пробуем раскрасить рёбра с maxDegree цветами
        if (canColorEdges(graph, maxDegree+1)) {
            return maxDegree;
        }

        // Если не получилось, пробуем с maxDegree + 1 цветами
        return canColorEdges(graph, maxDegree) ? maxDegree : -1;
    }

    // Находит максимальную степень вершины в графе.
    private int findMaxVertexDegree(Graph graph) {
        // ключ - id вершины, значение - степень вершины
        Map<Integer, Integer> vertexDegreeMap = new HashMap<>();

        //инициализируем нудями
        for (Vertex vertex : graph.getVertexList()) {
            vertexDegreeMap.put(vertex.getId(), 0);
        }

        // перебираем все ребра и считаем степени вершин
        for (Edge edge : graph.getEdgeList()) {
            int sourceId = edge.getSource();
            vertexDegreeMap.put(sourceId, vertexDegreeMap.get(sourceId) + 1);

            if (!graph.isDirect()) {
                int targetId = edge.getTarget();
                vertexDegreeMap.put(targetId, vertexDegreeMap.get(targetId) + 1);
            }
        }

        // Находим максимальную степень
        return vertexDegreeMap.values().stream()
                .max(Integer::compare)
                .orElse(0);
    }

    //Проверяет возможность раскраски рёбер графа заданным количеством цветов.
    private boolean canColorEdges(Graph graph, int numColors) {
        List<Edge> edges = new ArrayList<>(graph.getEdgeList());
        List<Vertex> vertices = graph.getVertexList();

        // Если цветов нет и рёбер нет - можно раскрасить
        if (numColors == 0) {
            return edges.isEmpty();
        }

        // Для каждой вершины храним набор используемых цветов
        HashMap<Integer, List<Integer>> vertexColors = new HashMap<Integer, List<Integer>>();
        for (Vertex vertex : vertices) {
            List<Integer> colors = new ArrayList<Integer>();
            vertexColors.put(vertex.getId(), colors);
        }

        return backtrackColoring(graph, edges, vertexColors, numColors, 0);
    }

    // Рекурсивный метод backtracking для проверки возможности раскраски

    private boolean backtrackColoring(Graph graph, List<Edge> edges,
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
            if (backtrackColoring(graph, edges, vertexColors, numColors, currentEdgeIndex + 1)) {
                return true;
            }

            // Откатываем изменения (backtrack)
            vertexColors.get(u).remove(color);
            vertexColors.get(v).remove(color);
        }

        return false;
    }


}
