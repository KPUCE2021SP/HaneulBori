//
//  SignUpContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct SignUpView: View {
    @State var id = ""  // Email
    @State var pw = ""  // Password
    @State var pwcheck = "" // Type password again
    @State var name = ""    // Name
    @State var bday = Date()    // Birthday
    @State var sex = "" // Sex
    @EnvironmentObject var viewModel: AppViewModel

    var body: some View {        
        VStack {
            Text("환영합니다! 회원 가입을 진행합니다.")
                .font(.title)
                .multilineTextAlignment(.center)
            TextField("ID", text : $id) // Id
                .padding()
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .autocapitalization(.none)
                .disableAutocorrection(true)
            SecureField("비밀번호", text : $pw)   // Password
                .padding(.horizontal)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .autocapitalization(.none)
                .disableAutocorrection(true)
            SecureField("비밀번호 확인", text : $pwcheck)  // Password checking
                .padding()
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .autocapitalization(.none)
                .disableAutocorrection(true)
            TextField("이름", text : $name)   // Name
                .padding(.horizontal)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .autocapitalization(.none)
            HStack {
                Text("유형")
                    .frame(width: 30)
                Button(action: {viewModel.type = "U"}) {    // Male
                    if viewModel.type != "U" {
                        Text("사용자")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                            .opacity(0.5)
                    }
                    else{
                        Text("사용자")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                    }
                }
                Button(action: {viewModel.type = "A"}) {    // Female
                    if viewModel.type != "A" {
                        Text("관리자")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                            .opacity(0.5)
                    }
                    else{
                        Text("관리자")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                    }
                }
            }
            .padding()
            HStack {
                Text("성별")
                    .frame(width: 30)
                Button(action: {sex = "M"}) {    // Male
                    if sex != "M" {
                        Text("남성")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                            .opacity(0.5)
                    }
                    else{
                        Text("남성")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                    }
                }
                Button(action: {sex = "F"}) {    // Female
                    if sex != "F" {
                        Text("여성")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                            .opacity(0.5)
                    }
                    else{
                        Text("여성")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 170)
                            .border(Color.black)
                            .background(Color.black)
                    }
                }
            }
            .padding(.horizontal)
//            Text("\(sex)")
            DatePicker(selection: $bday, displayedComponents: .date) {   // Birthday
                Text("생년월일")
            }
            .padding()
//          .datePickerStyle(WheelDatePickerStyle())
            Button(action: {
                viewModel.signUp(email: id, password: pw)
            }) {  // Complete signing up
                Text("회원 가입")
                    .foregroundColor(Color.white)
                    .padding()
                    .border(Color.black)
                    .background(Color.black)
            }
            NavigationLink(destination: SignInView()
                            .navigationBarHidden(true)
                            .navigationBarBackButtonHidden(true)){  // Sign up
                Text("취소")
                    .font(.caption)
            }
        }
    }
}

struct SignUpView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpView()
    }
}
