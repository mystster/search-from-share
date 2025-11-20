import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:receive_sharing_intent/receive_sharing_intent.dart';
import 'package:url_launcher/url_launcher.dart';

void main() {
  runApp(const MyApp());
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

class SearchPage extends StatefulWidget {
  const SearchPage({super.key});

  @override
  State<SearchPage> createState() => _SearchPageState();
}

class _SearchPageState extends State<SearchPage> {
  late StreamSubscription _intentDataStreamSubscription;

  @override
  void initState() {
    super.initState();

    // For sharing or opening urls/text coming from outside the app while the app is in the memory
    _intentDataStreamSubscription = ReceiveSharingIntent.instance
        .getMediaStream()
        .listen(
          (List<SharedMediaFile> value) {
            if (value.isNotEmpty && value.first.type == SharedMediaType.text) {
              _handleSharedText(value.first.path);
            }
          },
          onError: (err) {
            debugPrint("getIntentDataStream error: $err");
          },
        );

    // For sharing or opening urls/text coming from outside the app while the app is closed
    ReceiveSharingIntent.instance.getInitialMedia().then((
      List<SharedMediaFile> value,
    ) {
      if (value.isNotEmpty && value.first.type == SharedMediaType.text) {
        _handleSharedText(value.first.path);
      }
    });
  }

  @override
  void dispose() {
    _intentDataStreamSubscription.cancel();
    super.dispose();
  }

  Future<void> _handleSharedText(String text) async {
    if (text.isEmpty) return;

    // Remove URLs from the text
    final urlRegExp = RegExp(r'https?://\S+');
    var cleanText = text.replaceAll(urlRegExp, '').trim();

    // Remove surrounding quotes if present
    if (cleanText.startsWith('"') &&
        cleanText.endsWith('"') &&
        cleanText.length >= 2) {
      cleanText = cleanText.substring(1, cleanText.length - 1).trim();
    }

    if (cleanText.isEmpty) {
      // If only URL was shared, or text became empty, just close the app
      SystemNavigator.pop();
      return;
    }

    final Uri url = Uri.parse(
      'https://www.google.com/search?q=${Uri.encodeComponent(cleanText)}',
    );
    try {
      await launchUrl(url, mode: LaunchMode.externalApplication);
    } catch (e) {
      debugPrint('Could not launch $url: $e');
    } finally {
      // Close the app
      SystemNavigator.pop();
    }
  }

  @override
  Widget build(BuildContext context) {
    return const Scaffold(body: Center(child: CircularProgressIndicator()));
  }
}
