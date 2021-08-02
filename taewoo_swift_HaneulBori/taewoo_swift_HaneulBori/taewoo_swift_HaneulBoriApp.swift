//
//  taewoo_swift_HaneulBoriApp.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/19.
//

import SwiftUI
import Firebase

@main
struct taewoo_swift_HaneulBoriApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDeldgatae
    
    var body: some Scene {
        WindowGroup {
            let viewModel = AppViewModel()
            
            ContentView()
                .environmentObject(viewModel)
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        
        return true
    }
}
