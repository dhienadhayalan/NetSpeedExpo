import React, { useState } from 'react';
import { SafeAreaView, Text, TouchableOpacity, StyleSheet, NativeModules } from 'react-native';

const { SpeedService } = NativeModules;

export default function App() {
  const [running, setRunning] = useState(false);

  const startTracking = () => {
    try {
      SpeedService.start();
      setRunning(true);
    } catch (e) {
      console.log('Error starting service:', e);
    }
  };

  const stopTracking = () => {
    try {
      SpeedService.stop();
      setRunning(false);
    } catch (e) {
      console.log('Error stopping service:', e);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Internet Speed Tracker</Text>
      <Text style={styles.subtitle}>Foreground Service (10 sec updates)</Text>

      <Text style={styles.status}>
        Status: {running ? "RUNNING" : "STOPPED"}
      </Text>

      <TouchableOpacity style={[styles.button, styles.start]} onPress={startTracking}>
        <Text style={styles.btnText}>Start Tracking</Text>
      </TouchableOpacity>

      <TouchableOpacity style={[styles.button, styles.stop]} onPress={stopTracking}>
        <Text style={styles.btnText}>Stop Tracking</Text>
      </TouchableOpacity>

      <Text style={styles.note}>
        After START, check notifications. Internet speed updates every 10 seconds.
        Works even when app is minimized or locked.
      </Text>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#020617',
    padding: 24,
    justifyContent: 'center',
  },
  title: {
    fontSize: 26,
    fontWeight: '700',
    color: '#FFFFFF',
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 14,
    color: '#94A3B8',
    textAlign: 'center',
    marginTop: 8,
    marginBottom: 32,
  },
  status: {
    fontSize: 18,
    color: '#E5E7EB',
    textAlign: 'center',
    marginBottom: 24,
  },
  button: {
    paddingVertical: 16,
    borderRadius: 12,
    marginVertical: 8,
  },
  start: {
    backgroundColor: '#22C55E',
  },
  stop: {
    backgroundColor: '#EF4444',
  },
  btnText: {
    color: '#FFFFFF',
    fontSize: 18,
    fontWeight: '600',
    textAlign: 'center',
  },
  note: {
    color: '#64748B',
    fontSize: 12,
    marginTop: 24,
    textAlign: 'center',
  },
});
