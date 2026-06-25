# Pals UI

Pals UI is a publishable, performance-focused Jetpack Compose UI component library designed for modularity, scalability, and ease of maintenance. This project provides a collection of highly customizable, reusable components that follow consistent design patterns to simplify UI development in Android.

## Core Philosophy

The project is built on the principle that UI components should be small, focused, and easy to integrate. Each component is designed to be:

- **Modular**: Every component lives in its own module, ensuring a clean separation of concerns and enabling a flexible publishing strategy.
- **Performance-Oriented**: Leveraging Jetpack Compose best practices, such as immutable configuration specs and stable state hoisting, to ensure efficient recomposition and smooth UI performance.
- **Consistent**: All components adhere to a unified architecture using explicit concepts like **Specs** for configuration, **States** for runtime behavior, and **Defaults** for themed styling.
- **Scalable**: The project structure is designed to support a growing library of dozens of components without increasing complexity for individual developers.

## Project Structure

- **`app/`**: A showcase and sample application used to demonstrate and test the components in real-world scenarios.
- **`components/`**: The heart of the library, containing individual sub-projects for each UI component.

## Included Components

The library features a wide range of components, including:

- **AdaptiveStyledText**: A powerful text component for rendering multiple inline styles within a single paragraph.
- **StyledSwitch**: A highly customizable, animated toggle switch supporting custom shapes, icons, and themes.
- **StageIndicator**: A horizontal progress indicator for multi-step flows and paginated content.
- **PillTabRow**: A modern, pill-shaped tab navigation component.
- **Context Menu Card & Edge Accent Card**: Versatile card components for secondary actions and accented UI layouts.
- **AnimatedText**: Smoothly transitioning text for dynamic UI updates.
- **Vertical Page Switcher**: Optimized navigation for vertical scrolling layouts.

## Design Patterns

To maintain consistency and predictability across the library, every component follows these architectural guidelines:

- **Specs**: Immutable data classes used for all static configurations.
- **State**: Stable state objects for managing interactive runtime behaviors.
- **Defaults**: Pre-built, Material3-themed default configurations for quick integration.
- **Flat Structure**: Modules are kept intentionally flat for better discoverability and easier navigation.

Pals UI aims to provide developers with the building blocks needed to create beautiful, consistent, and high-performance Android applications with minimal boilerplate.
