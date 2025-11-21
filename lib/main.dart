import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_custom_tabs/flutter_custom_tabs.dart';
import 'package:receive_sharing_intent/receive_sharing_intent.dart';

void main() {
  runApp(const MyApp());
}

// Custom Tabs用のエントリポイント
@pragma('vm:entry-point')
void mainForCustomTabs() {
  runApp(const ShareApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Search from Share',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const SearchPage(),
    );
  }
}

class SearchPage extends StatelessWidget {
  const SearchPage({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(child: Text("Search from Share Settings")),
    );
  }
}

class ShareApp extends StatefulWidget {
  const ShareApp({super.key});

  @override
  State<ShareApp> createState() => _ShareAppState();
}

class _ShareAppState extends State<ShareApp> with WidgetsBindingObserver {
  String _sharedText = "";
  StreamSubscription? _intentDataStreamSubscription;
  bool _isCustomTabOpened = false;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);

    // For sharing or opening urls/text coming from outside the app while the app is in the memory
    _intentDataStreamSubscription = ReceiveSharingIntent.instance
        .getMediaStream()
        .listen(
          (value) {
            if (value.isNotEmpty && value.first.type == SharedMediaType.text) {
              setState(() {
                _sharedText = value.first.path;
              });
              _handleSharedText(_sharedText);
            }
          },
          onError: (err) {
            debugPrint("getIntentDataStream error: $err");
          },
        );

    // For sharing or opening urls/text coming from outside the app while the app is closed
    ReceiveSharingIntent.instance.getInitialMedia().then((value) {
      if (value.isNotEmpty && value.first.type == SharedMediaType.text) {
        setState(() {
          _sharedText = value.first.path;
        });
        _handleSharedText(_sharedText);
      } else {
        // 共有以外で起動された場合は終了
        SystemNavigator.pop();
      }
    });
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    _intentDataStreamSubscription?.cancel();
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed && _isCustomTabOpened) {
      _isCustomTabOpened = false;
      SystemNavigator.pop();
    }
  }

  Future<void> _handleSharedText(String text) async {
    if (text.isEmpty) {
      SystemNavigator.pop();
      return;
    }

    // URLを除去する
    // 改行 + URL のパターンを除去
    final urlRegex = RegExp(r'\nhttps?://\S+');
    String query = text.replaceAll(urlRegex, '').trim();

    // 引用符を除去する
    // 先頭と末尾が " で囲まれている場合のみ除去
    if (query.startsWith('"') && query.endsWith('"') && query.length >= 2) {
      query = query.substring(1, query.length - 1);
    }

    if (query.isEmpty) {
      SystemNavigator.pop();
      return;
    }

    final url = Uri.parse(
      'https://www.google.com/search?q=${Uri.encodeComponent(query)}',
    );

    try {
      _isCustomTabOpened = true;
      await launchUrl(
        url,
        customTabsOptions: const CustomTabsOptions(
          showTitle: true,
          urlBarHidingEnabled: true,
        ),
        safariVCOptions: const SafariViewControllerOptions(
          barCollapsingEnabled: true,
          dismissButtonStyle: SafariViewControllerDismissButtonStyle.close,
        ),
      );
    } catch (e) {
      debugPrint("Error launching Custom Tabs: $e");
      _isCustomTabOpened = false;
      SystemNavigator.pop();
    }
  }

  @override
  Widget build(BuildContext context) {
    // UIは表示しない（透明またはローディング）
    return MaterialApp(
      home: const Scaffold(body: Center(child: CircularProgressIndicator())),
      theme: ThemeData(useMaterial3: true),
    );
  }
}
