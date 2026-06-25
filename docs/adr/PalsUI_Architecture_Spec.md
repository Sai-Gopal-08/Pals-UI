# PalsUI - Architecture Decision Record

## Goal

Build a publishable Jetpack Compose UI component library that is:

- Modular
- Scalable at project level
- Easy to maintain
- Easy to publish
- Performance-focused
- Consistent across all reusable components

---

# Final Decision

## Project Scalability > Component Scalability

The primary growth axis is the overall library, not individual components.

Most reusable components (AnimatedText, Toggle, Button, etc.) are expected to remain relatively small and focused throughout their lifetime.

Therefore:

- Keep individual component architecture minimal.
- Scale through module organization.
- Avoid introducing package hierarchies inside every component unless genuinely required.

---

# Project Structure

```text
PalsUI/
│
├── app/                    # Showcase / sample app
│
├── components/
│   ├── animated_text/
│   ├── button/
│   ├── toggle/
│   ├── dialog/
│   └── ...
│
├── core/
│   ├── design/
│   ├── foundation/
│   └── testing/
│
└── docs/
```

## Reason

Benefits:

- Easy navigation
- Supports dozens of components
- Clear separation of reusable components and shared infrastructure
- Easy future publishing strategy

---

# Component Design Principles

Each component should expose only meaningful public concepts.

Preferred concepts:

- Component
- Spec
- State (when required)
- Defaults (when required)
- Callbacks (when required)

Examples:

```kotlin
AnimatedText()
AnimatedTextSpec
AnimatedTextState
AnimatedTextDefaults
AnimatedTextCallbacks
```

---

# Approaches Considered

## Option 1 - Deep Package Structure

```text
component/
├── api/
├── internal/
├── animation/
├── interaction/
├── layout/
├── state/
└── util/
```

### Pros

- Strong separation
- Suitable for very large features

### Cons

- Over-engineering for reusable UI components
- Excessive navigation
- More maintenance overhead

### Decision

Rejected.

Reason: Components are intentionally small and focused.

---

## Option 2 - Generic Model Folder

```text
component/
├── model/
└── Component.kt
```

### Pros

- Separates models from APIs

### Cons

- "model" becomes a catch-all bucket
- Intent is unclear
- Doesn't communicate whether files are Specs, States, DTOs, etc.

### Decision

Rejected.

Reason: Prefer explicit naming.

---

## Option 3 - Flat Structure (Chosen)

```text
component/
├── Component.kt
├── ComponentSpec.kt
├── ComponentState.kt
├── ComponentDefaults.kt
└── README.md
```

### Pros

- Minimal
- Readable
- Easy navigation
- No unnecessary folders

### Cons

- Can become crowded if component grows significantly

### Decision

Accepted.

Reason: Best balance between simplicity and maintainability.

---

# Folder Creation Rule

Folders should be introduced only when they provide real value.

Do NOT create folders preemptively.

Bad:

```text
component/
├── spec/
├── state/
├── callbacks/
```

when only one file exists in each folder.

Good:

Create folders only after file count and complexity justify them.

---

# Small Component Template (Recommended Default)

Example: AnimatedText

```text
animated_text/
├── AnimatedText.kt
├── AnimatedTextSpec.kt
├── AnimatedTextDefaults.kt
├── AnimationTransitionStyle.kt
├── TextAnimationStyle.kt
└── README.md
```

Characteristics:

- Flat structure
- Few files
- Easy to understand

---

# Large Component Template

Example: Carousel

```text
carousel/
├── spec/
│   ├── CarouselSpec.kt
│   ├── CarouselAnimationSpec.kt
│   └── CarouselIndicatorSpec.kt
│
├── state/
│   ├── CarouselState.kt
│   └── CarouselScrollState.kt
│
├── callbacks/
│   └── CarouselCallbacks.kt
│
├── Carousel.kt
├── CarouselDefaults.kt
└── README.md
```

Characteristics:

- Folders introduced only after complexity grows
- Folder names represent architecture concepts
- Avoid generic names such as model, data, common, util

---

# State Guidelines

If a component requires runtime state:

```kotlin
@Stable
class ComponentState
```

State:

- Can change
- Can trigger recomposition
- Represents runtime behavior

Do not create a state folder unless complexity requires it.

---

# Spec Guidelines

```kotlin
@Immutable
data class ComponentSpec(...)
```

Spec:

- Immutable
- Configuration only
- No mutable state
- No business logic
- No callbacks

---

# Callback Guidelines

Small number of callbacks:

```kotlin
onClick: () -> Unit
```

Many callbacks (roughly 4+):

```kotlin
ComponentCallbacks
```

Use immutable callback containers to avoid noisy APIs.

---

# Risks Considered

## Risk: Too Many Folders

Impact:

- Harder navigation
- Boilerplate architecture

Mitigation:

- Prefer flat structures first

---

## Risk: Flat Structure Becomes Crowded

Impact:

- Harder discoverability

Mitigation:

- Introduce spec/state/callback folders only when justified

---

## Risk: Generic Model Package

Impact:

- Ambiguous ownership
- Poor discoverability

Mitigation:

- Use explicit concepts (Spec, State, Callbacks)

---

# Reusable Component Validation Checklist

Before publishing a new reusable:

## Module

- [ ] Component has its own module
- [ ] README exists
- [ ] Component documentation added to `docs/components/`
- [ ] Sample/demo exists in showcase app
- [ ] `build.gradle.kts` includes `maven-publish`, `singleVariant("release")`, and `afterEvaluate` publishing blocks for JitPack compatibility

## API Design

- [ ] Public API is minimal
- [ ] Implementation details are hidden
- [ ] Naming follows library conventions
- [ ] Composable accepts a `Modifier` as the first optional parameter to allow external layout control
- [ ] Public API includes KDoc documentation for Spec and State properties

## Spec

- [ ] Spec exists when configuration is required
- [ ] Spec is immutable
- [ ] Spec contains configuration only

## State

- [ ] State exists only if needed
- [ ] State is stable
- [ ] State is properly hoisted

## Callbacks

- [ ] Small callback count uses direct lambdas
- [ ] Large callback count uses Callbacks container

## Compose Performance

- [ ] All composable parameters are stable types — UI recomposes only when inputs genuinely change
- [ ] No unnecessary recompositions
- [ ] Stable/Immutable annotations reviewed
- [ ] No avoidable allocations during recomposition
- [ ] No heavy computation or expensive logic directly in the Composable body (use `remember` or `derivedStateOf`)

## Design & Tooling

- [ ] Component uses `MaterialTheme` tokens (colors/typography); no hardcoded values unless specified in Spec
- [ ] Component provides proper Accessibility support (Content Descriptions, Semantics for interactive elements)
- [ ] At least one `@Preview` exists to verify the component in the IDE design surface

## Structure

- [ ] Flat structure used by default
- [ ] Additional folders justified by complexity
- [ ] No generic model/common/data folders

## Publishing

- [ ] Public API reviewed
- [ ] Documentation completed
- [ ] Component ready for Maven publication
