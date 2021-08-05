//
//  ReservationView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/28.
//

import SwiftUI

struct ReservationView: View {
    @State var time = Date()
    @State var isReserved1 = false
    @State var isReserved2 = false
    @EnvironmentObject var viewModel: AppViewModel
    
    var body: some View {
        NavigationView{
            VStack {
                Text("세탁기 예약")
                    .font(.title)
                Spacer()
                DatePicker(selection: $time, displayedComponents: .hourAndMinute){
                    Text("예약 시간")
    //                    .frame(width: 100.0)
                }
                .padding()
                .frame(width: 380.0)
    //            .datePickerStyle(WheelDatePickerStyle())
                Button(action: {
                    isReserved1 = true
                    viewModel.money += 900
                }){    // Reservation complete
                    if isReserved1 || viewModel.btn1state {
                        Text("1번 세탁기 예약하기")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 185)
                            .background(Color.black)
                            .opacity(0.5)
                            .border(Color.black)
                    }
                    else {
                        Text("1번 세탁기 예약하기")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 185)
                            .background(Color.black)
                            .border(Color.black)
                    }
                }
                .disabled(isReserved1 || viewModel.btn1state)
                Button(action: {
                    isReserved2 = true
                    viewModel.money += 900
                }){    // Reservation complete
                    if isReserved2 || viewModel.btn2state {
                        Text("2번 세탁기 예약하기")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 185)
                            .background(Color.black)
                            .opacity(0.5)
                            .border(Color.black)
                    }
                    else {
                        Text("2번 세탁기 예약하기")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 185)
                            .background(Color.black)
                            .border(Color.black)
                    }
                }
                .disabled(isReserved2 || viewModel.btn2state)
                NavigationLink(destination: UserView(state1: viewModel.state1, state2: viewModel.state2, btn1state: viewModel.btn1state, btn2state: viewModel.btn2state)
                                .navigationBarHidden(true)
                                .navigationBarBackButtonHidden(true)){  // Sign up
                    Text("취소")
                        .font(.caption)
                }
                Spacer()
            }
        }
    }
}

struct ReservationView_Previews: PreviewProvider {
    static var previews: some View {
        ReservationView()
    }
}
