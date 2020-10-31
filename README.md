# Functional Servlets

This project is an experiment in creating an algebra around Java's `HttpServlet` interface. It is meant to create separation between the essential complexity of producing a response and delivering the response payload. This ensures type safety of the response production as well as increased testability because responders generate a value versus a side effect.