// frontend/jest.config.cjs
module.exports = {
  testEnvironment: "node",
  roots: ["<rootDir>/js"],
  transform: {},                // no Babel, run as-is
  collectCoverage: true,
  collectCoverageFrom: ["<rootDir>/js/graph.js"],
  coverageDirectory: "<rootDir>/coverage",
  coverageReporters: ["text", "lcov", "html"]
  // coverageThreshold: { global: { branches: 90, functions: 90, lines: 90, statements: 90 } }
};
