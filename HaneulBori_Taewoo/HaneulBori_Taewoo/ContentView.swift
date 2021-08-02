//
//  ContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/19.
//

import SwiftUI
import FirebaseAuth

class AppViewModel: ObservableObject {
    let auth = Auth.auth()
    
    @Published var signedIn = false
    
    var isSignedIn: Bool {
        return auth.currentUser != nil
    }
    
    func signIn(email: String, password: String) {
        auth.signIn(withEmail: email, password: password) { [weak self] result, error in
            guard result != nil, error == nil
            else{
                return
            }
            
            DispatchQueue.main.async {
                self?.signedIn = true    // Success
            }
        }
    }
}

struct ContentView: View {
    @EnvironmentObject var viewModel: AppViewModel
    
    var body: some View {
        NavigationView{
//            SignInView()
            if viewModel.signedIn{
                UserView()
            }
            else {
                SignInView()
            }
        }
        .onAppear {
            viewModel.signedIn = viewModel.isSignedIn
        }
    }
}


struct SignInView: View {
    @State var id = ""  // Email
    @State var pw = ""  // Password
    
    @EnvironmentObject var viewModel: AppViewModel

    var body: some View {
        NavigationView{
            VStack {
                Text("로그인 후 이용 가능합니다!")
                    .font(.largeTitle)
                Spacer()
                TextField("ID", text : $id)   // Type ID
                    .padding([.top, .leading, .trailing])
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                SecureField("비밀번호", text : $pw)   // Type password
                    .padding(.horizontal)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                HStack {
//                    NavigationLink(destination: UserView()
//                                    .navigationBarHidden(true)
//                                    .navigationBarBackButtonHidden(true)){  // User
//                        Text("사용자 로그인")
//                            .foregroundColor(Color.white)
//                            .padding()
//                            .border(Color.black)
//                            .background(Color.black)
//                    }
                    Button(action: {
                        guard !id.isEmpty, !pw.isEmpty
                        else {
                            return
                        }
                        viewModel.signIn(email: id, password: pw)
                    }) {
                        Text("사용자 로그인")
                            .foregroundColor(Color.white)
                            .padding()
                            .border(Color.black)
                            .background(Color.black)
                    }
                    NavigationLink(destination: AdminView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){  // Administrator
                        Text("관리자 로그인")
                            .foregroundColor(Color.white)
                            .padding()
                    }
                    .border(Color.black)
                    .background(Color.black)
                }
                .padding()
                HStack {
                    Text("계정이 없으십니까?")
                    NavigationLink(destination: SignUpView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){  // Sign up
                        Text("회원가입")
                            .padding()
                    }
                }
                Spacer()
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
