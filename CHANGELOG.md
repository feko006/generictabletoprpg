# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

### Fixed

### Changed

### Removed

## [1.3.0]

### Added

### Fixed

- Editing a text trackable displaying a numeric instead of a text keyboard

### Changed

- Updated dependencies, gradle and AGP and Kotlin versions
- Migrated Compose Destinations to v2

### Removed

## [1.2.1]

### Added

- Added saving throw bonuses in the 5E stats dialog

## [1.2.0]

### Added

- Added buttons to scroll to top and bottom of a spell list
- Added 5e stats trackable

### Fixed

- Spell list scroll being reset when re-showing the dialog after casting, removing or inspecting a
  spell
- Not being able to edit a spell slot trackable when all slots have been used
- New trackables not being added to the end of the list

### Changed

- Updated base content with formatting improvements. Thanks to @cravl-dev !
- Updated dependencies, gradle and AGP, target and compile SDK versions
- The prepared spells checkbox now keeps its state when hiding and showing the dialog in a tracked
  group
- The dropdown for creating new trackables is sorted alphabetically and has a bit more horizontal
  breathing room

### Removed

## [1.1.1] - 2024-09-06

### Fixed

- Spell lists from version 1.0.0 not opening in version 1.1.x

### Changed

- Number of known spells now doesn't include cantrips in the spell list dialog

## [1.1.0] - 2024-09-03

### Added

- Option to mark spells in spell list as prepared and filter them by this flag

### Fixed

- Potential crash when importing tracked groups after clearing local storage or a fresh install

### Changed

- Spell UI in the spell list, casting time now shown

## [1.0.0] - 2024-07-27
