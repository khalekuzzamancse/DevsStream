import 'package:flutter/material.dart' as flutter;
import 'package:flutter/material.dart';

import 'modifier.dart';

//Put all in a single file so that easily can paste other projects
//If we use abstract class then we can not add overloaded operator method to
//abstract class as a result using DI or factory method it is not possible to use the overloaded
//operator method,that is why direcly using concrete implementation
//Since this just a small is wrapper and the Flutter built in widget itself is concrete so it make sense to use concrete implementation

class AsyncImage {
  String _link = "";
  Modifier _modifier = Modifier();

  AsyncImage link(String link) {
    _link = link;
    return this;
  }

  AsyncImage modifier(Modifier modifier) {
    _modifier = modifier;
    return this;
  }

  Widget build() {
    Widget rootLayout =
        Image.network(_link, fit: BoxFit.cover) //for keep image ratio same
            .modifier(_modifier);
    return rootLayout;
  }
}

/**
 * ## Examples
 *
 * ### Example 01:
 * ```dart
 * return BoxBuilder(
 *   modifier: Modifier()
 *     .background(_hexToColor(model.hexCode))
 *     .roundedCornerShape(8)
 *     .clickable(() { _showDialog(context); })
 * )
 * .child(
 *   child: Text(
 *     model.name,
 *     style: TextStyle(color: textColor),
 *   ),
 *   modifier: Modifier().paddingAll(4),
 * )
 * .build();
 * ```
 *
 * ### Example 02:
 * ```dart
 * (BoxBuilder(
 *   modifier: Modifier()
 *     .background(_hexToColor(model.hexCode))
 *     .roundedCornerShape(8)
 *     .clickable(() { _showDialog(context); })
 * )
 * + Text(
 *     model.name,
 *     style: TextStyle(color: textColor),
 *   ).modifier(Modifier().paddingAll(4))
 * ).build();
 * ```
 */

class BoxBuilder {
  Widget? _child;
  Modifier? _modifier = Modifier();

  BoxBuilder({Modifier? modifier}) : _modifier = modifier {}

  BoxBuilder child({required Widget child, Modifier? modifier = null}) {
    // Apply the modifier to the child if provided
    _child = modifier != null ? child.modifier(modifier) : child;
    return this;
  }

  BoxBuilder operator +(flutter.Widget widget) {
    return child(child: widget);
  }

  Widget build() {
    // Wrap the child with the modifier applied to the Box
    Widget box = _child ?? SizedBox.shrink();
    return box.modifier(_modifier ?? Modifier());
  }
}

class ButtonBuilder {
  String? _text;
  IconData? _icon;
  VoidCallback? _onPressed;
  ButtonStyle? _style;
  Modifier _buttonModifier = Modifier();
  Modifier _iconModifier = Modifier();
  Modifier _labelModifier = Modifier();

  bool _asTextButton = false;
  bool _asIconButton = false;

  ButtonBuilder label(String text) {
    _text = text;
    return this;
  }

  ButtonBuilder icon(IconData icon) {
    _icon = icon;
    return this;
  }

  ButtonBuilder onClick(VoidCallback onPressed) {
    _onPressed = onPressed;
    return this;
  }

  ButtonBuilder style(ButtonStyle style) {
    _style = style;
    return this;
  }

  ButtonBuilder asTextButton() {
    _asTextButton = true;
    return this;
  }

  ButtonBuilder asIconButton() {
    _asIconButton = true;
    return this;
  }

  ButtonBuilder buttonModifier(Modifier modifier) {
    _buttonModifier = modifier;
    return this;
  }

  ButtonBuilder iconModifier(Modifier modifier) {
    _iconModifier = modifier;
    return this;
  }

  ButtonBuilder labelModifier(Modifier modifier) {
    _labelModifier = modifier;
    return this;
  }

  Widget build() {
    Widget? button;

    if (_asTextButton) {
      button = TextButton(
        onPressed: _onPressed,
        style: _style,
        child: _text != null
            ? flutter.Text(_text!).modifier(_labelModifier)
            : const SizedBox.shrink(),
      );
    } else if (_asIconButton) {
      button = IconButton(
        onPressed: _onPressed,
        icon: _icon != null
            ? Icon(_icon).modifier(_iconModifier)
            : const SizedBox.shrink(),
      );
    } else {
      button = ElevatedButton.icon(
        onPressed: _onPressed,
        icon: _icon != null
            ? Icon(_icon).modifier(_iconModifier)
            : const SizedBox.shrink(),
        label: _text != null
            ? flutter.Text(_text!).modifier(_labelModifier)
            : const SizedBox.shrink(),
        style: _style,
      );
    }

    // Apply the modifier to the button itself
    return button.modifier(_buttonModifier);
  }
}

///
/// A layout strategy that arranges widgets vertically with configurable
/// horizontal alignment and spacing.
///
/// ## Examples
/// ### Example 1
/// ```dart
/// class _LayoutStrategy extends StatelessWidget {
///   const _LayoutStrategy({
///     required this.logoSection,
///     required this.description,
///     required this.staticInfo,
///     required this.labels,
///     required this.topics,
///   });
///
///   final Widget logoSection, description, staticInfo, labels, topics;
///
///   @override
///   Widget build(BuildContext context) {
///     return (
///       ColumnBuilder(
///         horizontalAlignment: CrossAxisAlignment.center,
///         arrangement: Arrangement.spaceBy(8),
///       )
///       + logoSection.modifier(Modifier().align(Alignment.center))
///       + description
///       + staticInfo
///       + topics
///       + labels
///     ).build();
///   }
/// }
/// ```
///
/// ### Example 2
/// ```dart
/// class _LayoutStrategy extends StatelessWidget {
///   const _LayoutStrategy({
///     required this.logoSection,
///     required this.description,
///     required this.staticInfo,
///     required this.labels,
///     required this.topics,
///   });
///
///   final Widget logoSection, description, staticInfo, labels, topics;
///
///   @override
///   Widget build(BuildContext context) {
///     return ColumnBuilder(
///       horizontalAlignment: CrossAxisAlignment.center,
///       arrangement: Arrangement.spaceBy(8),
///     )
///     .append(child: logoSection, modifier: Modifier().align(Alignment.center))
///     .append(child: description)
///     .append(child: staticInfo)
///     .append(child: topics)
///     .append(child: labels)
///     .build();
///   }
/// }
/// ```
/// ### Example 3: Adding child conditionally
/// ```dart
///   @override
///   Widget build(BuildContext context) {
///
///     return  (  ColumnBuilder(
///        horizontalAlignment: CrossAxisAlignment.start,
///         modifier: Modifier().wrapContentWidth())
///
///         + (avatar??EmptyWidget().modifier(Modifier().align(Alignment.center)))
///         + (_hasAvatar?VerticalSpacer(8):EmptyWidget())
///         + name
///         + (_hasName?VerticalSpacer(8):EmptyWidget())
///         + username
///         + VerticalSpacer(8)
///         + location
///         + (_hasLocation?VerticalSpacer(8):EmptyWidget())
///         + (bio?? EmptyWidget()) // +bio is also allowed
///         + (_hasBio?VerticalSpacer(8):EmptyWidget())
///         + (Row(children: [followers,HorizontalSpace(width:8),following])))
///
///     .build();
///
///   }
/// ```
/// ### Example 4: Shorted version of EX-3,
/// - Guaranteed null or EmptyWidget will be not added children list so we can use it as follow
///
/// ```dart
///   @override
///   Widget build(BuildContext context) {
///
///     return  (  ColumnBuilder(
///         horizontalAlignment: CrossAxisAlignment.start,
///         arrangement: Arrangement.spaceBy(8),
///         modifier: Modifier().wrapContentWidth())
///         + (avatar??EmptyWidget().modifier(Modifier().align(Alignment.center)))
///         + name
///         + username
///         + location
///         + bio
///         + (RowBuilder()+followers+HorizontalSpacer(8)+following).build())
///         .build();
///   }

class ColumnBuilder {
  final List<Widget> _children = [];
  final MainAxisAlignment _mainAxisAlignment;
  final CrossAxisAlignment _crossAxisAlignment;
  final Modifier? _modifier;
  final MainAxisSize _mainAxisSize = MainAxisSize.max;
  Arrangement _arrangement = Arrangement.Start;
  TextDirection? _textDirection;
  VerticalDirection _verticalDirection = VerticalDirection.down;
  TextBaseline? _textBaseline;

  ColumnBuilder({
    MainAxisAlignment verticalAlignment = MainAxisAlignment.center,
    CrossAxisAlignment horizontalAlignment = CrossAxisAlignment.center,
    Modifier? modifier,
    Arrangement arrangement = Arrangement.Start,
  })  : _mainAxisAlignment = verticalAlignment,
        _crossAxisAlignment = horizontalAlignment,
        _modifier = modifier,
        _arrangement = arrangement;

  /**
   * - Sometimes accepting null make the client code cleaner,though it causes side effect...
   * - It will help the client to prevent this: `append(nullableWidget??EmptyWidget())`, it can be written as
   * `append(nullableWidget)` so it reducing client code and also EmptyWidget is not added to UI tree
   * - For better readability client code pass the EmptyWidget, it will filter that
   */
  ColumnBuilder append({required Widget? child, Modifier? modifier}) {
    if (child == null || child is EmptyWidget) return this;
    Widget modifiedChild = modifier != null ? child.modifier(modifier) : child;
    _children.add(modifiedChild);
    return this;
  }

  /**
   * Useful to avoid unnecessary nesting
   * - UseCase 01: x==null?EmptyWidget else appendAll([y,spacer])
   */
  ColumnBuilder appendAll(List<flutter.Widget> children) {
    _children.addAll(children);
    return this;
  }

  /**
   * This method will helpful when consumer want the don't want to use the `append` prefix,
   * how ever since it capable to take one parameter,so if the child has Modifier then there is no option to pass the
   * modifier, instead direcly use the Modifier with the child
   */
  ColumnBuilder operator +(flutter.Widget? child) {
    return append(child: child);
  }

  Widget build() {
    Widget rootLayout = flutter.Column(
      mainAxisAlignment: _mainAxisAlignment,
      mainAxisSize: _mainAxisSize,
      crossAxisAlignment: _crossAxisAlignment,
      textDirection: _textDirection,
      verticalDirection: _verticalDirection,
      textBaseline: _textBaseline,
      children: _addSpaceIfAny(),
    );

    // Apply the modifier to the Column widget
    return rootLayout.modifier(_modifier ?? Modifier());
  }

  List<Widget> _addSpaceIfAny() {
    if (_arrangement is SpaceByArrangement) {
      final space = (_arrangement as SpaceByArrangement).value * 1.0;
      final List<Widget> spacedChildren = [];

      for (int i = 0; i < _children.length; i++) {
        spacedChildren.add(_children[i]);
        if (i < _children.length - 1) {
          spacedChildren.add(SizedBox(height: space));
        }
      }
      return spacedChildren;
    }
    return List<Widget>.from(_children);
  }
}

extension ColumnBuilderExtensions on ColumnBuilder {
  ColumnBuilder operator +(Child? child) {
    if (child != null) {
      append(child: child.widget, modifier: child.modifier);
    }
    return this;
  }
}

/**
 * - Used to override the '+' operator,since + operator accept only one argument
 */
class Child {
  final Widget? widget;
  final Modifier? modifier;

  Child(this.widget, {this.modifier});

  // A helper method to allow conditional addition
  static Child? when(bool condition, Widget widget, {Modifier? modifier}) {
    return condition ? Child(widget, modifier: modifier) : null;
  }
}

/**
 * - **Example 01**:
 * ```dart
 * class StatisticsRow extends StatelessWidget {
 *   final int watchers, openIssues, subscribers;
 *   const StatisticsRow({...}) : super(key: key);
 *
 *   @override
 *   Widget build(BuildContext context) {
 *     return (FlowRowBuilder(horizontalSpace: 8, verticalSpace: 8)
 *       + _StatisticTile(icon: Icons.visibility, count: watchers, label: 'Watchers')
 *       + _StatisticTile(icon: Icons.error_outline, count: openIssues, label: 'Issues')
 *       + _StatisticTile(icon: Icons.people, count: subscribers, label: 'Subscribers'))
 *       .build();
 *   }
 * }
 * ```
 *
 * - **Example 02**:
 * ```dart
 * class _TopicsSection extends StatelessWidget {
 *   final List<String> topics;
 *   const _TopicsSection({...}) : super(key: key);
 *
 *   @override
 *   Widget build(BuildContext context) {
 *     return FlowRowBuilder(horizontalSpace: 8, verticalSpace: 8)
 *       .appendAll(topics.map((topic) => Chip(
 *         label: Text(topic),
 *         backgroundColor: Theme.of(context).colorScheme.secondaryContainer,
 *       )).toList())
 *       .build();
 *   }
 * }
 * ```
 */

class FlowRowBuilder {
  final List<Widget> _children = [];
  double _horizontalSpacing = 0.0;
  double _verticalSpacing = 0.0;
  WrapAlignment _alignment = WrapAlignment.center; // Default alignment
  Modifier? _modifier = Modifier();
  final WrapCrossAlignment crossAxisAlignment;

  FlowRowBuilder(
      {double horizontalSpace = 0.0,
      double verticalSpace = 0.0,
      Modifier? modifier,
      WrapAlignment alignment = flutter.WrapAlignment.center,
         this.crossAxisAlignment=flutter.WrapCrossAlignment.center,
      })
      : _modifier = modifier,
        _verticalSpacing = verticalSpace,
        _horizontalSpacing = horizontalSpace,
        _alignment = alignment;

  FlowRowBuilder append({required Widget child, Modifier? modifier = null}) {
    Widget modifiedChild = modifier != null ? child.modifier(modifier) : child;
    _children.add(modifiedChild);
    return this;
  }

  FlowRowBuilder appendAll(List<Widget> children) {
    _children.addAll(children);
    return this;
  }

  /**
   * This method will helpful when consumer want the don't want to use the `append` prefix,
   * how ever since it capable to take one parameter,so if the child has Modifier then there is no option to pass the
   * modifier, instead direcly use the Modifier with the child
   */
  FlowRowBuilder operator +(flutter.Widget child) {
    return append(child: child);
  }

  FlowRowBuilder children(List<Widget> children) {
    _children.addAll(children);
    return this;
  }

  FlowRowBuilder spacer(double width) {
    _children.add(SizedBox(width: width));
    return this;
  }

  FlowRowBuilder horizontalSpacing(double spacing) {
    _horizontalSpacing = spacing;
    return this;
  }

  FlowRowBuilder verticalSpacing(double spacing) {
    _verticalSpacing = spacing;
    return this;
  }

  FlowRowBuilder childAlign(WrapAlignment alignment) {
    _alignment = alignment;
    return this;
  }

  FlowRowBuilder modifier(Modifier modifier) {
    _modifier = modifier;
    return this;
  }

  Widget build() {
    Widget flowRow = Wrap(
      spacing: _horizontalSpacing,
      runSpacing: _verticalSpacing,
      alignment: _alignment,
      crossAxisAlignment: crossAxisAlignment,
      children: _children,
    );
    return flowRow.modifier(_modifier ?? Modifier());
  }
}

enum Sizes { fillMaxWidth, wrapContentWidth }

abstract class Arrangement {
  const Arrangement();

  static const Start = const StartArrangement();
  static const End = const EndArrangement();

  factory Arrangement.spaceBy(int value) => SpaceByArrangement(value);
}

// Variant: Start arrangement
class StartArrangement extends Arrangement {
  const StartArrangement();
}

// Variant: End arrangement
class EndArrangement extends Arrangement {
  const EndArrangement();
}

// Variant: Equal weight arrangement
class EqualWeightArrangement extends Arrangement {
  const EqualWeightArrangement();
}

// Variant: Space by a specified value arrangement
class SpaceByArrangement extends Arrangement {
  final int value;

  const SpaceByArrangement(this.value);
}

/**
 * # Example
 * ```dart
 * @override
 * Widget build(BuildContext context) {
 *   return (RowBuilder(arrangement: Arrangement.spaceBy(8))
 *   + Text("hello").modifier(Modifier().x.y.z...n)
 *   + _StatisticTile(icon: Icons.error_outline, count: openIssues, label: 'Issues')
 *   + _StatisticTile(icon: Icons.people, count: subscribers, label: 'Subscribers'))
 *   .build();
 * }
 * ```
 *
 * - Or
 *
 * ```dart
 * @override
 * Widget build(BuildContext context) {
 *   return RowBuilder(arrangement: Arrangement.spaceBy(8))
 *   .append(child: Text("hello"), modifier: Modifier().x.y.z...n)
 *   .append(child: _StatisticTile(icon: Icons.error_outline, count: openIssues, label: 'Issues'))
 *   .append(child: _StatisticTile(icon: Icons.people, count: subscribers, label: 'Subscribers'))
 *   .build();
 * }
 * ```
 *
 */

//@formatter:off
class RowBuilder {
  final List<Widget> _children = [];
  MainAxisAlignment _mainAxisAlignment = MainAxisAlignment.start;
  CrossAxisAlignment _crossAxisAlignment = CrossAxisAlignment.center;
  Modifier? _modifier; // Field to store the modifier for the row
  Arrangement _arrangement=Arrangement.Start;
   MainAxisSize _mainAxisSize = flutter.MainAxisSize.min;
  TextDirection? _textDirection;
  VerticalDirection _verticalDirection = VerticalDirection.down;
  TextBaseline? _textBaseline;

  RowBuilder(
      {MainAxisAlignment horizontalAlignment = MainAxisAlignment.center,
      CrossAxisAlignment verticalAlignment = CrossAxisAlignment.center,
      Modifier? modifier,
      Sizes size = Sizes.wrapContentWidth,
        Arrangement  arrangement= Arrangement.Start,
      }): _mainAxisAlignment = horizontalAlignment,
        _crossAxisAlignment = verticalAlignment,
        _modifier = modifier,
        _arrangement=arrangement
  {
     if(size==Sizes.wrapContentWidth)
       _mainAxisSize=flutter.MainAxisSize.min;
     if(size==Sizes.fillMaxWidth)
       _mainAxisSize=flutter.MainAxisSize.max;



}

  RowBuilder append({required Widget child, Modifier? modifier = null}) {
    Widget modifiedChild = modifier != null ? child.modifier(modifier) : child;
    _children.add(modifiedChild);
    return this;
  }
  /**
   * This method will helpful when consumer want the don't want to use the `append` prefix,
   * how ever since it capable to take one parameter,so if the child has Modifier then there is no option to pass the
   * modifier, instead direcly use the Modifier with the child
   */
  RowBuilder operator +(flutter.Widget child) {
    return append(child: child);
  }

  RowBuilder spacer(double width) {
    _children.add(SizedBox(width: width));
    return this;
  }

  RowBuilder horizontalArrangement(MainAxisAlignment mainAxisAlignment) {
    _mainAxisAlignment = mainAxisAlignment;
    return this;
  }

  RowBuilder verticalAlignment(CrossAxisAlignment crossAxisAlignment) {
    _crossAxisAlignment = crossAxisAlignment;
    return this;
  }

  RowBuilder modifier(Modifier modifier) {
    _modifier = modifier;
    return this;
  }

  Widget build() {
    Widget row = flutter.Row(
      mainAxisAlignment: _mainAxisAlignment,
      mainAxisSize: _mainAxisSize,
      crossAxisAlignment: _crossAxisAlignment,
      textDirection: _textDirection,
      verticalDirection: _verticalDirection,
      textBaseline: _textBaseline,
      children: _addSpaceIfAny(),
    );

    // Apply the modifier to the entire Row widget
    return row.modifier(_modifier ?? Modifier());
  }
  List<Widget> _addSpaceIfAny() {
    if (_arrangement is SpaceByArrangement) {
      final space = (_arrangement as SpaceByArrangement).value * 1.0;
      final List<Widget> spacedChildren = [];

      for (int i = 0; i < _children.length; i++) {
        spacedChildren.add(_children[i]);
        if (i < _children.length - 1) {
          spacedChildren.add(SizedBox(width: space));
        }
      }
      return spacedChildren;
    }
    return List<Widget>.from(_children);
  }


}



class TextBuilder  {
  Modifier? _modifier;
  Key? _key;
  String? _data;
  TextStyle? _style;
  StrutStyle? _strutStyle;

  TextBuilder({Modifier? modifier}):_modifier=modifier{

  }

  TextBuilder text(String data) {
    _data = data;
    return this;
  }

  TextBuilder style(TextStyle? style) {
    _style = style;
    return this;
  }


  TextBuilder modifier(Modifier modifier) {
    _modifier = modifier;
    return this;
  }

  Widget build() {
    if (_data == null) {
      throw Exception('Text data is required');
    }
    return flutter.Text(
      _data!,
      key: _key,
      strutStyle: _strutStyle,
      style: _style,
    ).modifier(_modifier??Modifier());
  }
}



class FabBuilder {
  Key? _key;
  Widget? _child;
  String? _tooltip;
  Color? _foregroundColor;
  Color? _backgroundColor;
  Color? _focusColor;
  Color? _hoverColor;
  Color? _splashColor;
  double? _elevation;
  double? _focusElevation;
  double? _hoverElevation;
  double? _highlightElevation;
  double? _disabledElevation;
  VoidCallback? _onPressed;
  MouseCursor? _mouseCursor;
  bool _mini = false;
  ShapeBorder? _shape;
  final Clip _clipBehavior = Clip.none;
  FocusNode? _focusNode;
  final bool _autoicous = false;
  MaterialTapTargetSize? _materialTapTargetSize;
  final bool _isExtended = false;
  bool? _enableFeedback;


  FabBuilder key(Key key) {
    _key = key;
    return this;
  }


  FabBuilder icon(Widget child) {
    _child = child;
    return this;
  }


  FabBuilder clickListener(VoidCallback onPressed) {
    _onPressed = onPressed;
    return this;
  }


  FabBuilder background(Color color) {
    _backgroundColor = color;
    return this;
  }

  FabBuilder mini(bool mini) {
    _mini = mini;
    return this;
  }

  FabBuilder shape(ShapeBorder shape) {
    _shape = shape;
    return this;
  }

  FloatingActionButton build() {
    return FloatingActionButton(
      key: _key,
      tooltip: _tooltip,
      foregroundColor: _foregroundColor,
      backgroundColor: _backgroundColor,
      focusColor: _focusColor,
      hoverColor: _hoverColor,
      splashColor: _splashColor,
      elevation: _elevation,
      focusElevation: _focusElevation,
      hoverElevation: _hoverElevation,
      highlightElevation: _highlightElevation,
      disabledElevation: _disabledElevation,
      onPressed: _onPressed,
      mouseCursor: _mouseCursor,
      mini: _mini,
      shape: _shape,
      clipBehavior: _clipBehavior,
      focusNode: _focusNode,
      autofocus: _autoicous,
      materialTapTargetSize: _materialTapTargetSize,
      isExtended: _isExtended,
      enableFeedback: _enableFeedback,
      child: _child,
    );
  }
}


class CardBuilder {
  Color? _color;
  Color? _shadowColor;
  Color? _surfaceTintColor;
  int _elevation = 0; // Default non-null elevation
  ShapeBorder? _shape;
  bool _borderOnForeground = true;
  EdgeInsetsGeometry? _margin;
  Widget? _child;


  CardBuilder color(Color? color) {
    _color = color;
    return this;
  }


  CardBuilder shadowColor(Color? shadowColor) {
    _shadowColor = shadowColor;
    return this;
  }


  CardBuilder surfaceTintColor(Color? surfaceTintColor) {
    _surfaceTintColor = surfaceTintColor;
    return this;
  }


  CardBuilder elevation(int elevation) {
    _elevation = elevation; // Set integer elevation
    return this;
  }


  CardBuilder shape(ShapeBorder? shape) {
    _shape = shape;
    return this;
  }


  CardBuilder borderOnForeground(bool borderOnForeground) {
    _borderOnForeground = borderOnForeground;
    return this;
  }


  CardBuilder marginAll(double margin) {
    _margin = EdgeInsets.all(margin);
    return this;
  }


  CardBuilder child({required Widget child, Modifier? modifier = null}) {
    Widget modifiedChild = modifier != null ? child.modifier(modifier) : child;
    _child = modifiedChild;

    return this;
  }


  Widget build() {
    return Card(
      color: _color,
      shadowColor: _shadowColor,
      surfaceTintColor: _surfaceTintColor,
      elevation: _elevation.toDouble(),
      // Convert to double as required by Card widget
      shape: _shape,
      borderOnForeground: _borderOnForeground,
      margin: _margin,
      child: _child, // Child widget with applied modifier
    );
  }
}

class VerticalSpace extends StatelessWidget {
  final double height;

  const VerticalSpace({required this.height, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(height: height);
  }
}
class VSpacer extends StatelessWidget {
  final double height;
  const VSpacer( this.height);
  @override
  Widget build(BuildContext context)=>SizedBox(height: height);
}

class HorizontalSpace extends StatelessWidget {
  final double width;

  const HorizontalSpace({Key? key, required this.width}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(width: width);
  }
}
class HSpacer extends StatelessWidget {
  final double width;
  const HSpacer( this.width);

  @override
  Widget build(BuildContext context)=>SizedBox(width: width);
}

/**
 * - Empty Widget, used to instead of null
 */
class EmptyWidget extends StatelessWidget {
  const EmptyWidget({super.key});

  @override
  Widget build(BuildContext context) => flutter.SizedBox.shrink();
}
