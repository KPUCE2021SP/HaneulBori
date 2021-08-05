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
    var isAdmin = false
    var signedUp = false
    var state1 = "미사용"   // Washer No.1 state
    var state2 = "미사용"   // Washer No.2 state
    var btn1state = false    // Washer No.1 broken
    var btn2state = false    // Washer No.2 broken
    var money = 0 // Earned money
    
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
    
    func signUp(email: String, password: String) {
        auth.createUser(withEmail: email, password: password) { result, error in
            guard result != nil, error == nil
            else{
//                self.signinFailed = true
                return
            }
            DispatchQueue.main.async {
                self.signedIn = true    // Success
            }
        }
    }
    
    func signOut() {
        try? auth.signOut()
        self.signedIn = false
    }
}

struct ContentView: View {
    @EnvironmentObject var viewModel: AppViewModel
    
    var body: some View {
        NavigationView{

            if viewModel.signedIn {
                if viewModel.isAdmin {
                    AdminView()
                }
                else {
                    UserView(state1: viewModel.state1, state2: viewModel.state2, btn1state: viewModel.btn1state, btn2state: viewModel.btn2state)
                }
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
    @State var Tuser = true // User
    @State var Tadmin = false   // Administrator
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
                            .background(Color.black)
                            .border(Color.black)
                    }
                    Button(action: {
                        guard !id.isEmpty, !pw.isEmpty
                        else {
                            return
                        }
                        viewModel.isAdmin = true
                        viewModel.signIn(email: id, password: pw)
                    }) {    // Administrator
                        Text("관리자 로그인")
                            .foregroundColor(Color.white)
                            .padding()
                            .background(Color.black)
                            .border(Color.black)
                    }
//                    .alert(isPresented: $viewModel.signinFailed) {
//                        Alert(title: Text("ERROR"), message: Text("Please enter a valid email or password."), dismissButton: .default(Text("CLOSE")))
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

