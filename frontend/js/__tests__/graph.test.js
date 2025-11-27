import {
  Graph,
  validateGraphData,
  buildGraph,
  getNearbyCities,
  sampleData
} from "../graph.js";

describe("validateGraphData()", () => {
  test("valid dataset returns { ok: true }", () => {
    const res = validateGraphData(sampleData);
    expect(res).toEqual({ ok: true });
  });

  test("fails when cities/edges are not arrays", () => {
    expect(validateGraphData({ cities: null, edges: [] }).ok).toBe(false);
    expect(validateGraphData({ cities: [], edges: null }).ok).toBe(false);
  });

  test("fails on duplicate cities", () => {
    const data = { cities: ["A", "A"], edges: [] };
    const res = validateGraphData(data);
    expect(res.ok).toBe(false);
    expect(res.reason).toMatch(/duplicate/i);
  });

  test("fails on invalid city entry (non-string or blank)", () => {
    expect(validateGraphData({ cities: [""], edges: [] }).ok).toBe(false);
    expect(validateGraphData({ cities: [123], edges: [] }).ok).toBe(false);
  });

  test("fails when edge references unknown city", () => {
    const data = {
      cities: ["A", "B"],
      edges: [{ from: "A", to: "C", distance: 10 }]
    };
    const res = validateGraphData(data);
    expect(res.ok).toBe(false);
    expect(res.reason).toMatch(/unknown city/i);
  });

  test("fails on invalid distance (negative or not finite)", () => {
    const bad1 = { cities: ["A", "B"], edges: [{ from: "A", to: "B", distance: -1 }] };
    const bad2 = { cities: ["A", "B"], edges: [{ from: "A", to: "B", distance: NaN }] };
    expect(validateGraphData(bad1).ok).toBe(false);
    expect(validateGraphData(bad2).ok).toBe(false);
  });
});

describe("Graph class", () => {
  test("addCity validates input and stores adjacency list", () => {
    const g = new Graph();
    g.addCity("A");
    expect(g.adj.has("A")).toBe(true);

    expect(() => g.addCity("")).toThrow(/invalid city/i);
    expect(() => g.addCity(null)).toThrow(/invalid city/i);
  });

  test("addEdge requires known cities and valid distance; creates undirected edges", () => {
    const g = new Graph();
    g.addCity("A");
    g.addCity("B");

    g.addEdge("A", "B", 15);
    expect(g.neighbors("A")).toEqual([{ to: "B", distance: 15 }]);
    expect(g.neighbors("B")).toEqual([{ to: "A", distance: 15 }]);

    expect(() => g.addEdge("X", "B", 10)).toThrow(/unknown city/i);
    expect(() => g.addEdge("A", "Y", 10)).toThrow(/unknown city/i);
    expect(() => g.addEdge("A", "B", -5)).toThrow(/invalid distance/i);
    expect(() => g.addEdge("A", "B", NaN)).toThrow(/invalid distance/i);
  });

  test("neighbors requires known city", () => {
    const g = new Graph();
    g.addCity("A");
    expect(() => g.neighbors("B")).toThrow(/unknown city/i);
  });
});

describe("buildGraph()", () => {
  test("builds a Graph from cities+edges", () => {
    const g = buildGraph(["A", "B", "C"], [
      { from: "A", to: "B", distance: 5 },
      { from: "B", to: "C", distance: 7 }
    ]);
    expect(g).toBeInstanceOf(Graph);
    expect(g.neighbors("A")).toEqual([{ to: "B", distance: 5 }]);
    expect(g.neighbors("B")).toEqual(
      expect.arrayContaining([{ to: "A", distance: 5 }, { to: "C", distance: 7 }])
    );
  });
});

describe("getNearbyCities()", () => {
  const g = buildGraph(sampleData.cities, sampleData.edges);

  test("throws if graph is not a Graph instance", () => {
    expect(() => getNearbyCities({}, "Guadalajara", 50)).toThrow(/graph must be Graph/i);
  });

  test("returns [] for unknown destination or non-string destination", () => {
    expect(getNearbyCities(g, "Nope", 50)).toEqual([]);
    expect(getNearbyCities(g, null, 50)).toEqual([]);
  });

  test("filters by max distance and sorts ascending", () => {
    // Guadalajara neighbors: 10 (Tlaquepaque), 12 (Zapopan), 60 (Tequila), 78 (Tepatitlán)
    const within50 = getNearbyCities(g, "Guadalajara", 50);
    expect(within50.map(x => x.city)).toEqual(["Tlaquepaque", "Zapopan"]);
    expect(within50.map(x => x.distance)).toEqual([10, 12]);

    const within80 = getNearbyCities(g, "Guadalajara", 80);
    expect(within80.map(x => x.city)).toEqual(["Tlaquepaque", "Zapopan", "Tequila", "Tepatitlán"]);
    expect(within80.map(x => x.distance)).toEqual([10, 12, 60, 78]);
  });

  test("default max distance applies (250)", () => {
    const res = getNearbyCities(g, "Guadalajara");
    // all four neighbors should appear under default 250
    expect(res.length).toBeGreaterThanOrEqual(4);
    // ensure ascending order
    const dists = res.map(x => x.distance);
    expect([...dists].sort((a,b)=>a-b)).toEqual(dists);
  });
});
